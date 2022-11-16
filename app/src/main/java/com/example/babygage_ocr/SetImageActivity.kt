package com.example.babygage_ocr

import android.R
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.babygage_ocr.databinding.ActivitySetImageBinding


class SetImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivitySetImageBinding
        binding = ActivitySetImageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val textView = binding.tvImagePath
        val imageView = binding.ivImage

        val imagePath = intent.getStringExtra("path")
        textView.text = imagePath
        Glide.with(this).load(imagePath).into(imageView)


    }
}