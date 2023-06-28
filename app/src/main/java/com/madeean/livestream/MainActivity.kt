package com.madeean.livestream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.madeean.livestream.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
  private lateinit var binding:ActivityMainBinding
  private lateinit var videoAdapter: VideoPagingAdapter
  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityMainBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    setUpRecyclerView()
    insertData()
  }

  private fun insertData() {
    val listOfStreamKey: MutableList<String> = mutableListOf(
      "rtmp://10.4.77.122/live/cacingtanah"
    )
    videoAdapter.submitList(listOfStreamKey)
  }

  private fun setUpRecyclerView() {
    binding.apply {
      rvLivestream.layoutManager = LinearLayoutManager(this@MainActivity)
      videoAdapter = VideoPagingAdapter()
      rvLivestream.adapter = videoAdapter
    }
  }
}