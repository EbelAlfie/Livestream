package com.madeean.livestream

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.rtmp.RtmpDataSource
import androidx.media3.datasource.rtmp.RtmpDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.ExtractorsFactory
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.madeean.livestream.databinding.CustomPlayerUiBinding


class VideoPagingAdapter(private val port: Int) : RecyclerView.Adapter<VideoPagingAdapter.VideoViewHolder>() {
  private lateinit var context: Context
  private var BASE_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/"

  private val differCallback = object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
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
        val mediaItem = MediaItem.fromUri(BASE_URL + differ.currentList[position])
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

  fun submitList(listOfStreamKeys: List<String>) {
    differ.submitList(listOfStreamKeys)
  }

}