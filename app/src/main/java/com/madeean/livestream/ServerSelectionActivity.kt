package com.madeean.livestream

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.madeean.livestream.databinding.ActivityServerSelectBinding

class ServerSelectionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityServerSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply{
            btnSubmit.setOnClickListener {
                val port = Integer.getInteger(etPortInsertion.text.toString())
                LivestreamActivity.newInstanceActivity(this@ServerSelectionActivity,port)
            }
        }
    }
}