package com.example.babygage_ocr

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
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
        Log.d("test","${imagePath}")
        Glide.with(this).load(imagePath)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }
}