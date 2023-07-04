package com.madeean.livestream.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.madeean.livestream.databinding.CustomPlayerUiBinding
import com.madeean.livestream.domain.entity.LivestreamData


class VideoPagingAdapter(private val port: Int) : RecyclerView.Adapter<VideoPagingAdapter.VideoViewHolder>() {
  private lateinit var context: Context
  private var BASE_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/"

  private val differCallback = object : DiffUtil.ItemCallback<LivestreamData>(){
    override fun areItemsTheSame(oldItem: LivestreamData, newItem: LivestreamData): Boolean
    = oldItem.streamKey == newItem.streamKey
    override fun areContentsTheSame(oldItem: LivestreamData, newItem: LivestreamData): Boolean
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
    ExoPlayer.Builder(context)
      .build().also {
        holder.binding.pvVideoView.player = it
        val mediaItem = MediaItem.fromUri(BASE_URL + differ.currentList[position].streamKey)
        Toast.makeText(context, BASE_URL + differ.currentList[position], Toast.LENGTH_SHORT).show()
        val dataSourceFactory: DataSource.Factory = RtmpDataSource.Factory()
        val mediaSource: MediaSource =
          ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        it.setMediaSource(mediaSource)
        it.prepare()
        it.playWhenReady = true
      }
  }

  fun submitList(listOfStreamKeys: List<LivestreamData>) {
    differ.submitList(listOfStreamKeys)
  }

}