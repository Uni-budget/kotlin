package com.example.babygage_ocr


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        binding.financialBtn.setOnClickListener {
            val nextScreen = Intent(this, FinancialmainActivity::class.java)
            startActivity(nextScreen)
        }
        binding.takePictureBtn.setOnClickListener {
            val nextScreen = Intent(this, ImportimageActivity::class.java)
            startActivity(nextScreen)
        }
    }
}