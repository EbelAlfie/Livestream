package com.madeean.livestream

import android.content.Context
import android.view.View
import android.widget.TextView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class CustomPlayerUiController(
  private val context: Context,
  private val customPlayerUi: View,
  private val youTubePlayer: YouTubePlayer,
  private val youtubePlayerView:YouTubePlayerView
) : AbstractYouTubePlayerListener() {
  private lateinit var panel: View

  fun init(){
    initViews(customPlayerUi)
  }

  private fun initViews(playerUi: View) {
    panel = playerUi.findViewById(R.id.panel);
  }

}