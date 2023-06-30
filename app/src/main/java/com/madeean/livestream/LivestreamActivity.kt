package com.madeean.livestream

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.madeean.livestream.databinding.ActivityMainBinding
import kotlin.math.abs


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
    videoAdapter = VideoPagingAdapter(port)
    binding.vpLiveStream.apply {
      offscreenPageLimit = 1
      (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
      adapter = videoAdapter
    }
  }

  /*private fun setTransformation(vpImageSlider: ViewPager2) {
    val compositePageTransformer = CompositePageTransformer()
    compositePageTransformer.addTransformer { page, position ->
      val r = 1 - abs(position)
      page.scaleY = (0.80f + r * 0.20f)
    }
    vpImageSlider.setPageTransformer(compositePageTransformer)
  }*/

  private fun insertData() {
    val listOfStreamKey: MutableList<String> = mutableListOf(
      "cacingtanah",
      "nematoda"
    )
    videoAdapter.submitList(listOfStreamKey)
  }
}