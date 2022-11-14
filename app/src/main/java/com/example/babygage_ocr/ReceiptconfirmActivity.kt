package com.example.babygage_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityReceiptconfirmBinding

class ReceiptconfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityReceiptconfirmBinding
        binding = ActivityReceiptconfirmBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}