package com.example.babygage_ocr

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.babygage_ocr.databinding.ActivityHouseholdmainBinding
import android.widget.CalendarView
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class HouseholdmainActivity : AppCompatActivity() {

    var fname: String = ""
    var str: String = ""

    lateinit var binding: ActivityHouseholdmainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var binding: ActivityHouseholdmainBinding
        binding = ActivityHouseholdmainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.date.bringToFront()


        if(binding.diary.getText() == "") {
            binding.savebtn.visibility = View.VISIBLE // 저장 버튼이 Visible
            binding.editbtn.visibility = View.INVISIBLE // 수정 Button이 Invisible
            binding.deletebtn.visibility = View.INVISIBLE // 삭제 Button이 Invisible
        }
        else {
            binding.savebtn.visibility = View.INVISIBLE
            binding.editbtn.visibility = View.VISIBLE
            binding.deletebtn.visibility = View.VISIBLE
        }

        //날짜 형태
        val dateform: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        //date type (오늘 날짜)
        val date: Date = Date(binding.calendarView.date)

        binding.date.text = dateform.format(date)

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var day: String = "${year}년 ${month + 1}월 ${dayOfMonth}일"

            binding.date.text = day

            if(binding.diary.getText() == "") {
                binding.savebtn.visibility = View.VISIBLE // 저장 버튼이 Visible
                binding.diaryEditTxt.visibility = View.VISIBLE // EditText가 Visible
                binding.diary.visibility = View.INVISIBLE // 저장된 일기 textView가 Invisible
                binding.editbtn.visibility = View.INVISIBLE // 수정 Button이 Invisible
                binding.deletebtn.visibility = View.INVISIBLE // 삭제 Button이 Invisible

            }
            else {
                binding.savebtn.visibility = View.INVISIBLE
                binding.diaryEditTxt.visibility = View.INVISIBLE
                binding.diary.visibility = View.VISIBLE
                binding.editbtn.visibility = View.VISIBLE
                binding.deletebtn.visibility = View.VISIBLE
            }
//            binding.diaryEditTxt.setText("") // EditText에 공백값 넣기

            checkedDay(year, month, dayOfMonth) // checkedDay 메소드 호출
        }

        binding.savebtn.setOnClickListener { // save Button 클릭
            saveDiary(fname) // saveDiary 메소드 호출
            Toast.makeText(this, "fname + \"데이터를 저장했습니다.\"", Toast.LENGTH_SHORT).show()
            str = binding.diaryEditTxt.getText().toString() // str 변수에 edittext 내용을 String형으로 저장
            binding.diary.text = "${str}" // textView에 str 출력
            binding.savebtn.visibility = View.INVISIBLE
            binding.editbtn.visibility = View.VISIBLE
            binding.deletebtn.visibility = View.VISIBLE
            binding.diaryEditTxt.visibility = View.INVISIBLE
            binding.diary.visibility = View.VISIBLE
        }
    }


    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"  // 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        var fis: FileInputStream? = null // FileStream fis 변수 설정

        try {
            fis = openFileInput(fname) // fname 파일 오픈!!

            val fileData = ByteArray(fis.available()) // fileData에 파이트 형식으로 저장
            fis.read(fileData) // fileData를 읽음
            fis.close()

            str = String(fileData) // str 변수에 fileData를 저장

            binding.diaryEditTxt.visibility = View.INVISIBLE
            binding.diary.visibility = View.VISIBLE
            binding.diary.text = "${str}" // textView에 str 출력

            binding.savebtn.visibility = View.INVISIBLE
            binding.editbtn.visibility = View.VISIBLE
            binding.deletebtn.visibility = View.VISIBLE

            binding.editbtn.setOnClickListener { // 수정 버튼을 누를 시
                binding.diaryEditTxt.visibility = View.VISIBLE
                binding.diary.visibility = View.INVISIBLE
                binding.diaryEditTxt.setText(str) // editText에 textView에 저장된 내용을 출력
                binding.savebtn.visibility = View.VISIBLE
                binding.editbtn.visibility = View.INVISIBLE
                binding.deletebtn.visibility = View.INVISIBLE
                binding.diary.text = "${binding.diaryEditTxt.getText()}"
            }

            binding.deletebtn.setOnClickListener {
                binding.diary.visibility = View.INVISIBLE
                binding.diaryEditTxt.setText("")
                binding.diaryEditTxt.visibility = View.VISIBLE
                binding.savebtn.visibility = View.VISIBLE
                binding.editbtn.visibility = View.INVISIBLE
                binding.deletebtn.visibility = View.INVISIBLE
                removeDiary(fname)
                Toast.makeText(this, fname + "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show()
            }

            if(binding.diary.getText() == ""){
                binding.diary.visibility = View.INVISIBLE
//                diaryTextView.visibility = View.VISIBLE
                binding.savebtn.visibility = View.VISIBLE
                binding.editbtn.visibility = View.INVISIBLE
                binding.deletebtn.visibility = View.INVISIBLE
                binding.diaryEditTxt.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, MODE_NO_LOCALIZED_COLLATORS)
            var content: String = binding.diaryEditTxt.getText().toString()
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {
        var fos: FileOutputStream? = null

        try {
            fos = openFileOutput(readyDay, MODE_NO_LOCALIZED_COLLATORS)
            var content: String = ""
            fos.write(content.toByteArray())
            fos.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}