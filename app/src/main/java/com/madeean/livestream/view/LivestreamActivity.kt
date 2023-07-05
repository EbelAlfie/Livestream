package com.madeean.livestream.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.madeean.livestream.databinding.ActivityMainBinding
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.viewmodel.LivestreamViewModel


class LivestreamActivity : AppCompatActivity() {
  private lateinit var binding:ActivityMainBinding
  private lateinit var adapter: FragmentAdapter
  private var port: Int = 0
  private val BASE_URL: String = "rtmp://0.tcp.ap.ngrok.io:$port/live/"
  private lateinit var viewModel: LivestreamViewModel

  companion object {
    fun newInstanceActivity(context: Context, port: Int?) {
      val intent = Intent(context, LivestreamActivity::class.java)
      if (port != null) {
        intent.putExtra("port", port)
      }
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityMainBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    port = intent.getIntExtra("port", 0)

    viewModel = ViewModelProvider(
      this,
      ViewModelProvider.NewInstanceFactory()
    )[LivestreamViewModel::class.java]

    setViewPager()
    setObserver(viewModel)
  }

  private fun setObserver(viewModel: LivestreamViewModel) {
    viewModel.getLivestreamData().observe(this) {
      adapter.apply {
        for(i in it){
          addFragment(FragmentLiveStreaming(BASE_URL, i.streamKey))
        }
      }
    }
  }

  private fun setViewPager() {
    adapter = FragmentAdapter(
      supportFragmentManager, lifecycle
    )
    binding.vpLiveStream.adapter = adapter
  }
}