package com.madeean.livestream.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.madeean.livestream.R
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.products.model.ModelProductListDomain
import com.madeean.livestream.view.Utils.BASE_HLS_URL
import com.madeean.livestream.view.Utils.viewDisplayMode
import com.madeean.livestream.viewmodel.FragmentLiveViewModel
import com.madeean.livestream.viewmodel.ProductsViewModel
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class FragmentLiveStreaming(private val port: Int, private val streamKey: String) : Fragment() {
  private lateinit var binding: CustomPlayerUiBinding
  private val viewCountHandler = Handler(Looper.getMainLooper())
  private lateinit var viewModel: FragmentLiveViewModel
  private lateinit var chatAdapter: ChatAdapter
  private lateinit var productViewModel: ProductsViewModel
  private var listData: ArrayList<ModelProductListDomain> = arrayListOf()
  private lateinit var productHighlight: ModelProductListDomain

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

    disableSSLCertificateVerify() //buat streaming aniplay :)

    initViewInteractions()
    initPlayer()
    initRvCommentList()
    setObservers()
    getData()
  }

  private fun initRvCommentList() {
    binding.apply {
      rvChats.layoutManager = LinearLayoutManager(requireContext())
      chatAdapter = ChatAdapter()
      rvChats.adapter = chatAdapter
    }
  }

  private fun disableSSLCertificateVerify() {
    val trustAllCerts: Array<TrustManager> = arrayOf(
      object : X509TrustManager {
        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> {
          return arrayOf()
        }
      }
    )
    try {
      val sc: SSLContext = SSLContext.getInstance("SSL")
      sc.init(null, trustAllCerts, SecureRandom())
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
      HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
          return true
        }
      })
    } catch (e: KeyManagementException) {
      e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    }
  }

  private fun setObservers() {
    setObserverViewCount()
    setObserveProduct()
  }

  private fun getData() {
    getViewCount()
    getDataProduct()
  }

  private fun initViewInteractions() {
    binding.apply {
      ivClose.setOnClickListener {
        activity().onBackPressedDispatcher.onBackPressed()
      }
      tvComment.setOnClickListener {
        showCommentDialog()
      }
      viewIconShareLike.clShare.setOnClickListener {
        showPopupShare()
      }
      setOnClickBasket()
    }
  }

  private fun showPopupShare() {
    val sendIntent: Intent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT, "Alfagift Livestream")
      type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
  }

  private fun showCommentDialog() {
    CommentPopUpBottomSheetDialog.newInstance(object :
      CommentPopUpBottomSheetDialog.SetOnCommentSend {
      override fun onCommentSend(text: String) {
        chatAdapter.addChat(text)
      }
    }).show(
      requireActivity().supportFragmentManager,
      CommentPopUpBottomSheetDialog::class.java.simpleName
    )
  }

  private fun getDataProduct() {
    productViewModel.getActiveProduct()
  }

  private fun setObserveProduct() {
    productViewModel.products.observe(viewLifecycleOwner) {
      listData.clear()
      listData = it
      binding.btnBasketPopup.tvMiniBadge.text = listData.size.toString()

      var position = 0

      it.forEach { product ->
        if (product.isHighlight == 1) {
          position++
          productHighlight = ModelProductListDomain(
            id = product.id,
            name = product.name,
            image = product.image,
            productPrice = product.productPrice,
            productSpesialPrice = product.productSpesialPrice,
            productDiscount = product.productDiscount,
            isHighlight = product.isHighlight,
            isActive = product.isActive
          )
          setHighlightProductView(product)
          showProductPopup()
        }
      }

      if (position <= 0) {
        hideProductPopup()
      }
    }
  }

  private fun showProductPopup() {
    binding.viewProductPopup.highlightProduct.visibility = View.VISIBLE
  }

  private fun hideProductPopup() {
    binding.viewProductPopup.highlightProduct.visibility = View.GONE
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

  private fun setObserverViewCount() {
    viewModel.getLivestreamViewCount().observe(viewLifecycleOwner) {
      binding.layoutLiveView.tvViews.text = it.toString()
    }
  }

  private fun setHighlightProductView(product: ModelProductListDomain) {
    binding.viewProductPopup.apply {
      Glide.with(this@FragmentLiveStreaming).load(product.image)
        .placeholder(requireActivity().getDrawable(R.drawable.default_image)).into(imgProduct)
      txtProductName.text = product.name
      txtProductPrice.text = product.productPrice.toString()
      viewDiscount.tvDiscount.text = product.productDiscount.toString()
      viewDiscount.tvDiscountLabel.text = product.productSpesialPrice.toString()
    }
  }


  @SuppressLint("UnsafeOptInUsageError")
  private fun initPlayer() {
    val exoplayer = ExoPlayer.Builder(requireContext()).build()
    binding.pvVideoView.player = exoplayer
    exoplayer.apply {
      val mediaItem = buildMediaItem()
      setMediaSource(createDataSource(mediaItem))

      trackSelectionParameters = trackSelectionParameters.buildUpon()
        .setMaxVideoSizeSd()
        .build()

      addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
          when (playbackState) {
            Player.STATE_READY -> {}
            Player.STATE_BUFFERING -> {}
            Player.STATE_ENDED -> { showEndStream(binding) }
            Player.STATE_IDLE -> { prepare() }
          }
        }
        override fun onPlayerError(error: PlaybackException) {
          super.onPlayerError(error)
          when(error.errorCode) {
            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW ->
              seekToDefaultPosition()
            else -> toastPrint(error.errorCodeName)
          }
          prepare()
        }
      })
      playWhenReady = true
    }
  }

  override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode)
    binding.apply {
      productContainer.visibility = viewDisplayMode(isInPictureInPictureMode)
      layoutLiveView.viewCountContainer.visibility = viewDisplayMode(isInPictureInPictureMode)
      viewIconShareLike.clLove.visibility = viewDisplayMode(isInPictureInPictureMode)
      viewIconShareLike.clIconProduct.visibility = viewDisplayMode(isInPictureInPictureMode)
      viewIconShareLike.clShare.visibility = viewDisplayMode(isInPictureInPictureMode)
      ivClose.visibility = viewDisplayMode(isInPictureInPictureMode)
      viewHost.containerHost.visibility = viewDisplayMode(isInPictureInPictureMode)
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
    val hlsMediaSource = HlsMediaSource.Factory(defaultHttpDataSourceFactory)
      .createMediaSource(mediaItem)
    return hlsMediaSource
  }

  private fun buildMediaItem(): MediaItem {
    return MediaItem.Builder().setUri(BASE_HLS_URL)
      .setLiveConfiguration(
        MediaItem.LiveConfiguration.Builder()
          .setMaxPlaybackSpeed(1.02f)
          .setMaxOffsetMs(1000).build()
      ) //TODO set offsetnya biar ga terlalu delay
      .setMimeType(MimeTypes.APPLICATION_MPD).setMimeType(MimeTypes.APPLICATION_MATROSKA)
      .setMimeType(MimeTypes.APPLICATION_M3U8)
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

  private fun toastPrint(s: String) {
    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
  }

  override fun onDestroy() {
    viewModel.postViewCount(streamKey, false)
    super.onDestroy()
  }

  override fun onResume() {
    super.onResume()
    viewModel.postViewCount(streamKey, true)
    binding.pvVideoView.player?.apply{
      seekToDefaultPosition()
      prepare()
    }
  }

  override fun onPause() {
    super.onPause()
    viewModel.postViewCount(streamKey, false)
    binding.pvVideoView.player?.stop()
  }
  private fun Long.toSecond(): Long = this / 1000

  private fun activity(): LivestreamActivity {
    return requireActivity() as LivestreamActivity
  }
}