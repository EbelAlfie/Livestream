package com.madeean.livestream

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.madeean.livestream.databinding.ActivityMainBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions


class MainActivity : AppCompatActivity() {
  private lateinit var binding:ActivityMainBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityMainBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    lifecycle.addObserver(binding.youtubePlayerView)

    val customPlayerUi: View =binding.youtubePlayerView.inflateCustomPlayerUi(R.layout.custom_player_ui)

    val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
      override fun onReady(youTubePlayer: YouTubePlayer) {
        val customPlayerUiController:CustomPlayerUiController = CustomPlayerUiController(
          this@MainActivity,
          customPlayerUi,
          youTubePlayer,
          binding.youtubePlayerView
        )
        customPlayerUiController.init()
        youTubePlayer.addListener(customPlayerUiController)
        youTubePlayer.loadVideo("yabDCV4ccQs",0f)
      }

    }

    val options:IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
    binding.youtubePlayerView.initialize(listener,options)

  }
}