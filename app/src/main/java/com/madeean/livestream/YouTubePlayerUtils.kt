package com.madeean.livestream

import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

object YouTubePlayerUtils {
//  fun YouTubePlayer.loadOrCueVideo(lifecycle: Lifecycle, videoId: String, startSeconds: Float) {
//    loadOrCueVideo(lifecycle.currentState == Lifecycle.State.RESUMED, videoId, startSeconds)
//  }

  fun loadOrCueVideo(youTubePlayer :YouTubePlayer, lifecycle: Lifecycle, videoId: String, startSeconds: Float) {
    loadOrCueVideo(youTubePlayer, lifecycle, videoId, startSeconds)
  }


  @JvmSynthetic
  internal fun YouTubePlayer.loadOrCueVideo(canLoad: Boolean, videoId: String, startSeconds: Float) {
    if (canLoad)
      loadVideo(videoId, startSeconds)
    else
      cueVideo(videoId, startSeconds)
  }

}