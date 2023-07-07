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
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.products.model.ModelProductList
import com.madeean.livestream.domain.products.model.ModelProductListDomain
import com.madeean.livestream.viewmodel.FragmentLiveViewModel
import com.madeean.livestream.viewmodel.LivestreamViewModel
import com.madeean.livestream.viewmodel.ProductsViewModel

class FragmentLiveStreaming(private val port: Int, private val streamKey: String): Fragment() {
    private lateinit var binding : CustomPlayerUiBinding
    private val viewCountHandler = Handler(Looper.getMainLooper())
    private lateinit var viewModel: FragmentLiveViewModel
    private lateinit var productViewModel: ProductsViewModel
    private val BASE_RTMP_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/" //"https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"//"rtmp://0.tcp.ap.ngrok.io:$port/live/"
    private val TEST_DASH_URL = "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"
    private lateinit var listData:ArrayList<ModelProductListDomain>
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
        setDataItem()
        initView()
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
        binding.pvVideoView.player = exoplayer
        exoplayer.apply {
            val mediaItem = buildMediaItem(TEST_DASH_URL)
            setMediaSource(createDataSource(mediaItem))

            // Update the track selection parameters to only pick standard definition tracks
            trackSelectionParameters = trackSelectionParameters.buildUpon()
                .setMaxVideoSizeSd()
                .build()

            addListener(object: Player.Listener {
                override fun onIsLoadingChanged(isLoading: Boolean) {
                    super.onIsLoadingChanged(isLoading)
                }

                override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
                    super.onAvailableCommandsChanged(availableCommands)
                    if(availableCommands.contains(Player.COMMAND_SEEK_TO_DEFAULT_POSITION))
                        seekToDefaultPosition()
                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when(playbackState) {
                        Player.STATE_READY -> {
                            if (isCommandAvailable(Player.COMMAND_SEEK_TO_DEFAULT_POSITION))
                                seekToDefaultPosition()
                            toastPrint("ready")
                        }
                        Player.STATE_BUFFERING -> {
                            toastPrint("buffer")}
                        Player.STATE_ENDED -> {
                            toastPrint("end")
                            showEndStream(binding)}
                        Player.STATE_IDLE -> {
                            toastPrint("idle")
                            prepare() }
                    }
                }
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    toastPrint(error.toString())
                    prepare()
                }
            })

            prepare()
            playWhenReady = true
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun createDataSource(mediaItem: MediaItem): MediaSource {
        val rtmpDatasourceFactory = RtmpDataSource.Factory() //transfer listener
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()

        val rtmpMediaSource = ProgressiveMediaSource.Factory(rtmpDatasourceFactory)
            .createMediaSource(mediaItem)
        val dashMediaSource = DashMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(mediaItem)
        return dashMediaSource
    }

    private fun Long.toSecond(): Long = this/ 1000000

    private fun buildMediaItem(url: String): MediaItem {
        return MediaItem.Builder().setUri(url)
            .setLiveConfiguration(
                MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build()
            )
            .setMimeType(MimeTypes.APPLICATION_MPD).setMimeType(MimeTypes.APPLICATION_MATROSKA)
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
            }
        , 5000)
    }

    private fun showEndStream(binding: CustomPlayerUiBinding) {
        binding.tvStreamEnd.visibility = View.VISIBLE
    }

    private fun toastPrint(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        viewModel.postViewCount(streamKey, false)
        super.onDestroy()
    }
}