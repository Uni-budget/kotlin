package com.example.babygage_ocr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityIntroselectionBinding

class IntroselectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityIntroselectionBinding
        binding = ActivityIntroselectionBinding.inflate(layoutInflater)

        setContentView(binding.root)



        binding.householdbtn.setOnClickListener{
            val nextScreen = Intent(this, HouseholdmainActivity::class.java)
            startActivity(nextScreen)
        }

        binding.financialbtn.setOnClickListener{
            val nextScreen = Intent(this, FinancialmainActivity::class.java)
            startActivity(nextScreen)
        }
    }
}