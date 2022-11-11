package com.example.babygage_ocr


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityFinancialmainBinding



class FinancialmainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityFinancialmainBinding
        binding = ActivityFinancialmainBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        binding.takePictureBtn.setOnClickListener {
//            val nextScreen = Intent(applicationContext, TakepictureActivity::class.java)
//            startActivity(nextScreen)
//        }
//
//        binding.mypageBtn.setOnClickListener {
//            val nextScreen = Intent(applicationContext, MypageActivity::class.java)
//            startActivity(nextScreen)
//        }
//        binding.householdBtn.setOnClickListener {
//            val nextScreen = Intent(applicationContext, HouseholdmainActivity::class.java)
//            startActivity(nextScreen)
//        }
        binding.financialBtn.setOnClickListener {
            val nextScreen = Intent(this, FinancialmainActivity::class.java)
            startActivity(nextScreen)
        }
    }
}