package com.example.babygage_ocr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babygage_ocr.databinding.ActivitySuccessfulsignupBinding

class SuccessfulsignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivitySuccessfulsignupBinding
        binding = ActivitySuccessfulsignupBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.loginpagebtn.setOnClickListener(View.OnClickListener {
            val nextScreen = Intent(this, LoginActivity::class.java)
            startActivity(nextScreen)
        })
    }
}