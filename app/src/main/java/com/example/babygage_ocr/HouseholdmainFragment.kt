package com.example.babygage_ocr

import android.annotation.SuppressLint
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.babygage_ocr.databinding.FragmentFinancialmainBinding
import com.example.babygage_ocr.databinding.FragmentHouseholdmainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var firebaseAuth: FirebaseAuth
private lateinit var firestore: FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 * Use the [HouseholdmainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HouseholdmainFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null


    var fname: String = ""
    var str: String = ""
    lateinit var binding: FragmentHouseholdmainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHouseholdmainBinding.inflate(inflater, container, false)
        setUpView()


        // Inflate the layout for this fragment
        return binding.root
    }
    private fun setUpView() {
//        binding.date.bringToFront()
//        //날짜 형태
//        val dateform: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
//        //date type (오늘 날짜)
//        val date: Date = Date(binding.calendarView.date)
//
//        binding.date.text = dateform.format(date)
//
//        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
//            var day: String = "${year}년 ${month + 1}월 ${dayOfMonth}일"
//
//            binding.date.text = day
//
//        }


        firebaseAuth= FirebaseAuth.getInstance()
        val currentUser = firebaseAuth?.currentUser
        firestore= FirebaseFirestore.getInstance()
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
            str = binding.diaryEditTxt.getText().toString() // str 변수에 edittext 내용을 String형으로 저장
            binding.diary.text = "${str}" // textView에 str 출력
            binding.savebtn.visibility = View.INVISIBLE
            binding.editbtn.visibility = View.VISIBLE
            binding.deletebtn.visibility = View.VISIBLE
            binding.diaryEditTxt.visibility = View.INVISIBLE
            binding.diary.visibility = View.VISIBLE
            var userDiary = UserDiary()
            userDiary.uid = firebaseAuth.currentUser!!.uid
            userDiary.useId = firebaseAuth.currentUser!!.email
            userDiary.diary = binding.diary.text.toString()
            userDiary.date = binding.date.text.toString()
            firestore?.collection("${firebaseAuth.currentUser!!.uid.toString()} diary")?.document(binding.date.text.toString())?.set(userDiary)
            Toast.makeText(getActivity(),"save diary",Toast.LENGTH_SHORT).show()
        }

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
            firestore.collection("${firebaseAuth.currentUser!!.uid.toString()} diary")?.document(binding.date.text.toString())?.delete()
            Toast.makeText(getActivity(), "delete diary", Toast.LENGTH_SHORT).show()
        }

        binding.importReceipt.setOnClickListener (({
            val nextScreen = Intent(context, TestActivity::class.java)
            startActivity(nextScreen)
            activity?.finish()
        })
        )
    }


    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {
        fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"  // 저장할 파일 이름 설정. Ex) 2019-01-20.txt
        var fis: FileInputStream? = null // FileStream fis 변수 설정

        try {
            fis = getActivity()?.openFileInput(fname) // fname 파일 오픈!!

            val fileData = ByteArray(fis!!.available()) // fileData에 파이트 형식으로 저장
            fis.read(fileData) // fileData를 읽음
            fis.close()

            str = String(fileData) // str 변수에 fileData를 저장

            binding.diaryEditTxt.visibility = View.INVISIBLE
            binding.diary.visibility = View.VISIBLE
            binding.diary.text = "${str}" // textView에 str 출력

            binding.savebtn.visibility = View.INVISIBLE
            binding.editbtn.visibility = View.VISIBLE
            binding.deletebtn.visibility = View.VISIBLE



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






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HouseholdmainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HouseholdmainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}