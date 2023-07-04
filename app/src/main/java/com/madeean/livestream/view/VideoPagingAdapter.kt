package com.madeean.livestream.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.media3.common.MediaItem
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
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.viewmodel.LivestreamViewModel
import java.util.*


class VideoPagingAdapter(private val port: Int, private val listener: SetOnUpdatedItem) : RecyclerView.Adapter<VideoPagingAdapter.VideoViewHolder>() {
  private lateinit var context: Context
  private var BASE_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/"

  private val differCallback = object : DiffUtil.ItemCallback<LivestreamKeysData>(){
    override fun areItemsTheSame(oldItem: LivestreamKeysData, newItem: LivestreamKeysData): Boolean
    = oldItem.streamKey == newItem.streamKey
    override fun areContentsTheSame(oldItem: LivestreamKeysData, newItem: LivestreamKeysData): Boolean
    = oldItem.streamKey == newItem.streamKey
  }
  private val differ = AsyncListDiffer(this, differCallback)

  class VideoViewHolder(val binding: CustomPlayerUiBinding): ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
    context = parent.context
    return VideoViewHolder(
      CustomPlayerUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun getItemCount(): Int {
    return differ.currentList.size
  }

  @SuppressLint("UnsafeOptInUsageError")
  override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
    val data = differ.currentList[position]
    val exoplayer = ExoPlayer.Builder(context).build()
    exoplayer.also {
      holder.binding.pvVideoView.player = it

      val mediaItem = MediaItem.fromUri(BASE_URL + data.streamKey)
      val mediaSource: MediaSource =
        ProgressiveMediaSource.Factory(RtmpDataSource.Factory())
          .createMediaSource(mediaItem)

      it.addListener(object: Player.Listener {
        override fun onIsLoadingChanged(isLoading: Boolean) {
          super.onIsLoadingChanged(isLoading)
          showProgressBar(holder.binding.progressLoading, isLoading)
        }
        override fun onPlaybackStateChanged(playbackState: Int) {
          when(playbackState) {
            Player.STATE_READY -> {
              showProgressBar(holder.binding.progressLoading, true)
              listener.onViewCountPost(data.streamKey, true)
              Toast.makeText(context, "ready", Toast.LENGTH_SHORT).show()
            }
            Player.STATE_BUFFERING -> {Toast.makeText(context, "buffer", Toast.LENGTH_SHORT).show()}
            Player.STATE_ENDED -> {showEndStream(holder.binding)}
            Player.STATE_IDLE -> {it.prepare()}
          }
        }
        override fun onPlayerError(error: PlaybackException) {
          super.onPlayerError(error)
          Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
          it.prepare()
        }
      })
      it.setMediaSource(mediaSource)
      it.prepare()
      it.playWhenReady = true
    }

    Timer().schedule
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

  interface SetOnUpdatedItem {
    fun getViewCount()
    fun onViewCountPost(streamKey: String, isViewing: Boolean)
  }

}