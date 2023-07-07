package com.madeean.livestream.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline.Window
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.offline.DownloadHelper.createMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.products.model.ModelProductList
import com.madeean.livestream.viewmodel.FragmentLiveViewModel
import com.madeean.livestream.viewmodel.LivestreamViewModel

class FragmentLiveStreaming(private val port: Int, private val streamKey: String) : Fragment() {
  private lateinit var binding: CustomPlayerUiBinding
  private val viewCountHandler = Handler(Looper.getMainLooper())
  private lateinit var viewModel: FragmentLiveViewModel
    private val BASE_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/" //"https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"//"rtmp://0.tcp.ap.ngrok.io:$port/live/"
    private lateinit var listData:ArrayList<ModelProductList>
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
    productViewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    )[ProductsViewModel::class.java]

    viewModel.postViewCount(streamKey, true)

    setObserver()
    initPlayer()
    getViewCount()

    setObserveProduct()
    getDataProduct()
    setOnClickBasket()
  }

  private fun getDataProduct() {
    productViewModel.getActiveProduct()
  }



  private fun setObserveProduct() {
    productViewModel.products.observe(viewLifecycleOwner) {
      listData.clear()
      listData = it
      binding.btnBasketPopup.tvMiniBadge.text = listData.size.toString()
    }
  }

  private fun setOnClickBasket() {
    binding.btnBasketPopup.containerImage.setOnClickListener {
      ProductPopUpBottomSheetDialog.newInstance(listData)
        .show(
          requireActivity().supportFragmentManager,
          ProductPopUpBottomSheetDialog::class.java.simpleName
        )
    }
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

            toastPrint(BASE_URL + streamKey)

            val mediaItem = buildMediaItem(BASE_URL + streamKey)

            /**RTMP*/
            val datasource = RtmpDataSource.Factory() //transfer listener
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(datasource)
                .createMediaSource(mediaItem)
            /**DASH*/
            /*val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
            // Create a dash media source pointing to a dash manifest uri.
            val mediaSource: MediaSource =
                DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            */
      addListener(object : Player.Listener {
        override fun onIsLoadingChanged(isLoading: Boolean) {
          super.onIsLoadingChanged(isLoading)
                    showProgressBar(binding.progressLoading, isLoading)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
          when (playbackState) {
            Player.STATE_READY -> {
                            showProgressBar(binding.progressLoading, false)
                            if (exoplayer.isCommandAvailable(
                                Player.COMMAND_SEEK_TO_DEFAULT_POSITION)) {
                seekToDefaultPosition()
                                }
              toastPrint("ready")
            }
            Player.STATE_BUFFERING -> {
                            //seekTo(currentLiveOffset)
                            toastPrint("buffer")}
            Player.STATE_ENDED -> {
              toastPrint("end")
              showEndStream(binding)
            }
            Player.STATE_IDLE -> {
              toastPrint("idle")
              prepare()
            }
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

    private fun Long.toSecond(): Long {
        return this/ 1000000
    }

  private fun buildMediaItem(url: String): MediaItem {
        return MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_MATROSKA)
      .setLiveConfiguration(
        MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build()
      )
      .build()
  }

  private fun getViewCount() {
    viewCountHandler.postDelayed(
      object : Runnable {
        override fun run() {
          viewModel.getLiveViewCount(streamKey)
          getDataProduct()
          viewCountHandler.postDelayed(this, 5000)
        }
      }, 5000
    )
  }

  private fun showEndStream(binding: CustomPlayerUiBinding) {
    binding.tvStreamEnd.visibility = View.VISIBLE
  }

    private fun showProgressBar(progressBar: ProgressBar, isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

  private fun toastPrint(s: String) {
    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
  }

  override fun onDestroy() {
    viewModel.postViewCount(streamKey, false)
    super.onDestroy()
  }
}