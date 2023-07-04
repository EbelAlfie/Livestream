package com.madeean.livestream.view

import android.os.Bundle
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
                val data = etPortInsertion.text.toString()
                if (data.isBlank()) return@setOnClickListener
                val port = Integer.valueOf(data)
                LivestreamActivity.newInstanceActivity(this@ServerSelectionActivity, port)
            }
        }
    }
}