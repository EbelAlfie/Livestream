package com.madeean.livestream.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.madeean.livestream.R
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.viewmodel.LivestreamViewModel
import java.util.*


class VideoPagingAdapter(private val port: Int, private val listener: SetOnUpdatedItem) : RecyclerView.Adapter<VideoPagingAdapter.VideoViewHolder>() {
  private lateinit var holder: VideoViewHolder
  private lateinit var context: Context
  private val BASE_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/"

  private val viewCountHandler = Handler(Looper.getMainLooper())

  private val differCallback = object : DiffUtil.ItemCallback<LivestreamKeysData>(){
    override fun areItemsTheSame(oldItem: LivestreamKeysData, newItem: LivestreamKeysData): Boolean
    = oldItem.streamKey == newItem.streamKey
    override fun areContentsTheSame(oldItem: LivestreamKeysData, newItem: LivestreamKeysData): Boolean
    = oldItem.streamKey == newItem.streamKey
  }
  private val differ = AsyncListDiffer(this, differCallback)

  interface SetOnUpdatedItem {
    fun getViewCount(streamKey: String)
    fun onViewCountPost(streamKey: String, isViewing: Boolean)
  }

  class VideoViewHolder(val binding: CustomPlayerUiBinding): ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
    context = parent.context
    holder = VideoViewHolder(
      CustomPlayerUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    return holder
  }

  override fun getItemCount(): Int {
    return differ.currentList.size
  }

  override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
    val data = differ.currentList[position]
    initPlayer(holder.binding, data.streamKey)
    getViewCount(data.streamKey)
  }

  @SuppressLint("UnsafeOptInUsageError")
  private fun initPlayer(binding: CustomPlayerUiBinding, streamKey: String) {
    val exoplayer = ExoPlayer.Builder(context).build()
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

  private fun getViewCount(streamKey: String) {
    viewCountHandler.postDelayed(
      object : Runnable {
        override fun run() {
          listener.getViewCount(streamKey)
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

  fun submitList(listOfStreamKeys: List<LivestreamKeysData>) {
    differ.submitList(listOfStreamKeys)
  }

  private fun toastPrint(s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
  }

  fun updateText(it: Int) {
    holder.binding.layoutLiveView.tvViews.text = it.toString()
  }

}