package com.example.babygage_ocr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityHouseholdMypageBinding

class HouseholdMypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding : ActivityHouseholdMypageBinding
        binding = ActivityHouseholdMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.barchart.setOnClickListener{
            val nextScreen = Intent(this, HouseholdChartActivity::class.java)
            startActivity(nextScreen)
        }

    }
}