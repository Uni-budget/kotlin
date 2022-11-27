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
            val nextScreen = Intent(this, MainActivity::class.java)
            // fragment 선택 위해 값 넘기기
            nextScreen.putExtra("select","household")
            startActivity(nextScreen)
        }

        binding.financialbtn.setOnClickListener{
            val nextScreen = Intent(this, MainActivity::class.java)
            // fragment 선택 위해 값 넘기기
            nextScreen.putExtra("select","financial")
            startActivity(nextScreen)
        }
    }
}