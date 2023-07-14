package com.madeean.livestream.view

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
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

    setOnBackPressedDispatcher()

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

  private fun setOnBackPressedDispatcher() {
    onBackPressedDispatcher.addCallback {
      if (checkPipPermission()) enterPictureInPictureMode()
      else finish()
    }
  }

  private fun checkPipPermission(): Boolean {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager?
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps?.unsafeCheckOpNoThrow(
          AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
          android.os.Process.myUid(),
          packageName
        )
      } else {
        @Suppress("DEPRECATION")
        appOps?.checkOpNoThrow(
          AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
          android.os.Process.myUid(),
          packageName
        )
      }
      mode == AppOpsManager.MODE_ALLOWED
    } else {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }
  }

}