package com.madeean.livestream.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.madeean.livestream.databinding.ActivityMainBinding
import com.madeean.livestream.domain.entity.LivestreamData
import com.madeean.livestream.viewmodel.LivestreamViewModel


class LivestreamActivity : AppCompatActivity() {
  private lateinit var binding:ActivityMainBinding
  private lateinit var videoAdapter: VideoPagingAdapter
  private var port: Int = 0
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

    setUpRecyclerView()
    setObserver(viewModel)
  }

  private fun setObserver(viewModel: LivestreamViewModel) {
    viewModel.getLivestreamData().observe(this) {
      insertData(it)
    }
  }

  private fun setUpRecyclerView() {
    videoAdapter = VideoPagingAdapter(port)
    binding.vpLiveStream.apply {
      offscreenPageLimit = 1
      (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
      adapter = videoAdapter
    }
  }

  private fun insertData(listOfStreamKey: List<LivestreamData>) {
    Log.d("test", listOfStreamKey.toString())
    videoAdapter.submitList(listOfStreamKey)
  }
}