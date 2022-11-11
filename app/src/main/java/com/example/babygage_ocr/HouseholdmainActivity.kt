package com.example.babygage_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityHouseholdmainBinding

class HouseholdmainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityHouseholdmainBinding
        binding = ActivityHouseholdmainBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }
}