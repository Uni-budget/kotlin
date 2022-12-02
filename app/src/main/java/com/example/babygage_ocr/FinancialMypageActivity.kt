package com.example.babygage_ocr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.babygage_ocr.databinding.ActivityFinancialMypageBinding

class FinancialMypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityFinancialMypageBinding
        binding = ActivityFinancialMypageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val receive_intent = intent

        val temp = receive_intent.getStringExtra("key01")
        val temp2 = receive_intent.getStringExtra("key02")
        val temp3 = receive_intent.getStringExtra("key03")

        binding.rowTextResult.setText(temp)
        binding.rowTextResult2.setText(temp2)
        binding.rowTextResult3.setText(temp3)

        binding.barchart.setOnClickListener{
            val nextScreen = Intent(this, FinancialChartActivity::class.java)
            startActivity(nextScreen)
        }
    }
}