package com.example.babygage_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityCameraBinding
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}