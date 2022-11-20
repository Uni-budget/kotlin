package com.example.babygage_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.babygage_ocr.databinding.ActivityHouseholdmainBinding
import android.widget.CalendarView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class HouseholdmainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityHouseholdmainBinding
        binding = ActivityHouseholdmainBinding.inflate(layoutInflater)

        setContentView(binding.root)

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

    }
}