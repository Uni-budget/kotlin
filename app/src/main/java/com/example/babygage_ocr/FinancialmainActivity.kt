package com.example.babygage_ocr


import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.babygage_ocr.databinding.ActivityFinancialmainBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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

        binding.date.bringToFront()
        //날짜 형태
        val dateform : DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        //date type (오늘 날짜)
        val date : Date = Date(binding.calendarView.date)

        binding.date.text = dateform.format(date)

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var day : String = "${year}년 ${month+1}월 ${dayOfMonth}일"

            binding.date.text = day
        }
        binding.takePictureBtn.setOnClickListener {
            val nextScreen = Intent(this, ImportimageActivity::class.java)
            startActivity(nextScreen)
        }

    }
}