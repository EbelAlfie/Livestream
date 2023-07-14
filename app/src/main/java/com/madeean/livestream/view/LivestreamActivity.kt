package com.madeean.livestream.view

import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import com.madeean.livestream.databinding.ActivityMainBinding
import com.madeean.livestream.viewmodel.LivestreamViewModel


class LivestreamActivity : AppCompatActivity() {
  private lateinit var binding:ActivityMainBinding
  private lateinit var adapter: FragmentAdapter
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

    setViewPager()
    setObserver()
  }

  private fun setObserver() {
    viewModel.getLivestreamData().observe(this) {
      if (it.isEmpty()) Toast.makeText(this, "No streams at the moment", Toast.LENGTH_SHORT).show()
      for(i in it){
        val videoFragment = FragmentLiveStreaming(port, i.streamKey)
        Bundle().apply {
          putInt("port", port)
          putString("key", i.streamKey)
          videoFragment.arguments = this
        }
        adapter.addFragment(videoFragment)
      }
    }
  }

  private fun setViewPager() {
    adapter = FragmentAdapter(supportFragmentManager, lifecycle)
    binding.vpLiveStream.adapter = adapter
  }

  override fun onBackPressed() {
    enterPictureInPictureMode()
  }

}