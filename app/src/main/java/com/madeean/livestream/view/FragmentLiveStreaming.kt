package com.madeean.livestream.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.viewmodel.FragmentLiveViewModel
import com.madeean.livestream.viewmodel.LivestreamViewModel

class FragmentLiveStreaming(private val BASE_URL: String, private val streamKey: String): Fragment() {
    private lateinit var binding : CustomPlayerUiBinding
    private val viewCountHandler = Handler(Looper.getMainLooper())
    private lateinit var viewModel: FragmentLiveViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomPlayerUiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FragmentLiveViewModel::class.java]

        viewModel.postViewCount(streamKey, true)

        setObserver()
        initPlayer()
        getViewCount()
    }

    private fun setObserver() {
        viewModel.getLivestreamViewCount().observe(viewLifecycleOwner) {
            binding.layoutLiveView.tvViews.text = it.toString()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initPlayer() {
        val exoplayer = ExoPlayer.Builder(requireContext()).build()
        exoplayer.apply {
            binding.pvVideoView.player = this

            //val mediaItem = MediaItem.fromUri(BASE_URL + streamKey)
            val mediaItem = MediaItem.Builder()
                .setUri(BASE_URL + streamKey)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()

            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(RtmpDataSource.Factory())
                .createMediaSource(mediaItem)

            addListener(object: Player.Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    super.onIsLoadingChanged(isLoading)
                    showProgressBar(binding.progressLoading, isLoading)
                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when(playbackState) {
                        Player.STATE_READY -> {
                            showProgressBar(binding.progressLoading, false)
                            toastPrint("ready")
                        }
                        Player.STATE_BUFFERING -> {toastPrint("buffer")}
                        Player.STATE_ENDED -> {
                            toastPrint("end")
                            showEndStream(binding)}
                        Player.STATE_IDLE -> {
                            toastPrint("idle")
                            prepare()}
                    }
                }
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    toastPrint(error.toString())
                    prepare()
                }
            })

            // Update the track selection parameters to only pick standard definition tracks
            trackSelectionParameters = trackSelectionParameters
                .buildUpon()
                .setMaxVideoSizeSd()
                .build()

            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true
        }
    }

    private fun getViewCount() {
        viewCountHandler.postDelayed(
            object : Runnable {
                override fun run() {
                    viewModel.getLiveViewCount(streamKey)
                    viewCountHandler.postDelayed(this, 5000)
                }
            }
            , 5000)
    }

    private fun showEndStream(binding: CustomPlayerUiBinding) {
        binding.rlVideoContainer.visibility = View.GONE
        binding.rlStreamEnded.visibility = View.VISIBLE
    }

    private fun showProgressBar(progressBar: ProgressBar, isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun toastPrint(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        viewModel.postViewCount(streamKey, false)
        super.onDestroy()
    }
}