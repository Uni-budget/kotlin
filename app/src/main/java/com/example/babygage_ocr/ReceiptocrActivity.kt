package com.example.babygage_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityReceiptocrBinding

class ReceiptocrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityReceiptocrBinding
        binding = ActivityReceiptocrBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}