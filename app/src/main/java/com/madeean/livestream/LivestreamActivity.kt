package com.madeean.livestream

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.madeean.livestream.databinding.ActivityMainBinding


class LivestreamActivity : AppCompatActivity() {
  private lateinit var binding:ActivityMainBinding
  private lateinit var videoAdapter: VideoPagingAdapter
  private var port: Int = 0

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

    setUpRecyclerView()
    insertData()
  }

  private fun setUpRecyclerView() {
    binding.apply {
      rvLivestream.layoutManager = LinearLayoutManager(this@LivestreamActivity)
      videoAdapter = VideoPagingAdapter(port)
      rvLivestream.adapter = videoAdapter
    }
  }

  private fun insertData() {
    val listOfStreamKey: MutableList<String> = mutableListOf(
      "cacingtanah"
    )
    videoAdapter.submitList(listOfStreamKey)
  }
}