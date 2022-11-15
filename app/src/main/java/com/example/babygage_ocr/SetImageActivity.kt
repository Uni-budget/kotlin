package com.example.babygage_ocr

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.babygage_ocr.databinding.ActivitySetImageBinding
import java.text.SimpleDateFormat


class SetImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivitySetImageBinding
        binding = ActivitySetImageBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }
}