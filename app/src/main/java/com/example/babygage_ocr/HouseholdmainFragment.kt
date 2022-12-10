package com.example.babygage_ocr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.babygage_ocr.databinding.FragmentHouseholdmainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
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


        firebaseAuth= FirebaseAuth.getInstance()
        val currentUser = firebaseAuth?.currentUser
        firestore= FirebaseFirestore.getInstance()

        //handle layout of date text
//        binding.date.bringToFront()

        //날짜 형태
        val dateform: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        //date type (오늘 날짜)
        val date: Date = Date(binding.calendarView.date)
        var today = LocalDate.now()

        binding.date.text = dateform.format(date)

        Log.d("ITM",binding.date.text.toString())
        val docRef = firestore.collection("${firebaseAuth.currentUser!!.email.toString()} diary").document(binding.date.text.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d("ITM", document.data.toString())
                if(document.data?.get("date")==binding.date.text.toString()) {
                    Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                    Log.d("ITM", "DocumentSnapshot data: ${document.data?.get("date")}")
                    binding.diary.text = document.data?.get("diary").toString()
                    binding.diaryEditTxt.setText(document.data?.get("diary").toString())
                    binding.savebtn.visibility = View.INVISIBLE
                    binding.editbtn.visibility = View.VISIBLE
                    binding.deletebtn.visibility = View.VISIBLE
                    binding.diaryEditTxt.visibility = View.INVISIBLE
                    binding.diary.visibility = View.VISIBLE
                }
                else {
                    Log.d("ITM", "No such document")
                    binding.diary.text = null
                    binding.diaryEditTxt.setText(null)
                    binding.savebtn.visibility = View.VISIBLE // 저장 버튼이 Visible
                    binding.editbtn.visibility = View.INVISIBLE // 수정 Button이 Invisible
                    binding.deletebtn.visibility = View.INVISIBLE // 삭제 Button이 Invisible
                    binding.diaryEditTxt.visibility = View.VISIBLE
                    binding.diary.visibility = View.INVISIBLE
                }
            }


        // total expenditure
        var docsize = 0
        var sum = 0
        binding.expenditure.bringToFront()
        firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}").get()
            //${firebaseAuth.currentUser!!.email.toString()}
            .addOnSuccessListener { snap ->
                Log.d("ITM", "size of document : ${snap.size()}")
                docsize = snap.size()
                for (i: Int in 0..docsize-1) {
                    val receiptRef =
                        firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}")
                            .document("Household_Receipts${i}")
                    receiptRef.get()
                        .addOnSuccessListener { document ->
//                            Log.d("ITM","document date = ${document.data?.get("date")}")
//                            Log.d("ITM","date text = ${day_reciept}")
                            if (document.data?.get("date") == today) {
                                sum = sum + document.data?.get("price").toString().toInt()
                                Log.d("ITM", sum.toString())
                            } else {
                                Log.d("ITM", "not on that date")
                            }
                        }
                }
                binding.expenditure.text = "${sum}원"
            }


        binding.calendarView.setOnDateChangeListener { calendarView, year : Int, month : Int, dayOfMonth : Int ->
            var day: String = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            var day_reciept = ""
            if(dayOfMonth.toString().length<2) {
                day_reciept = "${year}${month + 1}0${dayOfMonth}"
                Log.d("ITM", "day_receipt : $day_reciept")
            }
            else {
                day_reciept = "${year}${month + 1}${dayOfMonth}"
                Log.d("ITM", "day_receipt : $day_reciept")
            }

            binding.date.text = day
            sum = 0
            binding.expenditure.text = "${sum}원"
            firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}").get()
                //${firebaseAuth.currentUser!!.email.toString()}
                .addOnSuccessListener { snap ->
                    Log.d("ITM", "size of document : ${snap.size()}")
                    docsize = snap.size()
                    for (i: Int in 0..docsize-1) {
                        val receiptRef =
                            firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}")
                                .document("Household_Receipts${i}")
                        receiptRef.get()
                            .addOnSuccessListener { document ->
                                Log.d("ITM","document date = ${document.data?.get("date")}")
                                Log.d("ITM","date text = ${day_reciept}")
                                if (document.data?.get("date") == day_reciept) {
                                    sum = sum + document.data?.get("price").toString().toInt()
                                    Log.d("ITM", sum.toString())
                                    binding.expenditure.text = "${sum}원"
                                    Log.d("ITM",binding.expenditure.text.toString())
                                } else {
                                    Log.d("ITM", "not on that date")
                                }
                            }
                    }
                }
            val docRef = firestore.collection("${firebaseAuth.currentUser!!.email.toString()} diary").document(binding.date.text.toString())
            docRef.get()
                .addOnSuccessListener { document ->
                    if(document.data?.get("date")==binding.date.text.toString()) {
                        Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                        Log.d("ITM", "DocumentSnapshot data: ${document.data?.get("date")}")
                        binding.diary.text = document.data?.get("diary").toString()
                        binding.diaryEditTxt.setText(document.data?.get("diary").toString())
                        binding.savebtn.visibility = View.INVISIBLE
                        binding.editbtn.visibility = View.VISIBLE
                        binding.deletebtn.visibility = View.VISIBLE
                        binding.diaryEditTxt.visibility = View.INVISIBLE
                        binding.diary.visibility = View.VISIBLE
                    }
                    else {
                        Log.d("ITM", "No such document")
                        binding.diary.text = null
                        binding.diaryEditTxt.setText(null)
                        binding.savebtn.visibility = View.VISIBLE // 저장 버튼이 Visible
                        binding.editbtn.visibility = View.INVISIBLE // 수정 Button이 Invisible
                        binding.deletebtn.visibility = View.INVISIBLE // 삭제 Button이 Invisible
                        binding.diaryEditTxt.visibility = View.VISIBLE
                        binding.diary.visibility = View.INVISIBLE
                    }
                }
        }

        binding.savebtn.setOnClickListener { // save Button 클릭
            val docRef = firestore.collection("${firebaseAuth.currentUser!!.email.toString()} diary").document(binding.date.text.toString())
            docRef.get()
                .addOnSuccessListener { document ->
                    if(document.data?.get("date")==binding.date.text.toString()) {
                        Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                        Log.d("ITM", "DocumentSnapshot data: ${document.data?.get("date")}")
                        binding.diary.text = binding.diaryEditTxt.text.toString()
                        var userDiary = UserDiary()
                        userDiary.uid = firebaseAuth.currentUser!!.uid
                        userDiary.useId = firebaseAuth.currentUser!!.email
                        userDiary.diary = binding.diary.text.toString()
                        userDiary.date = binding.date.text.toString()
                        firestore?.collection("${firebaseAuth.currentUser!!.email.toString()} diary")?.document(binding.date.text.toString())?.set(userDiary)
                        Toast.makeText(getActivity(),"save diary",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.d("ITM", "No such document")
                        binding.diary.text = binding.diaryEditTxt.text.toString()
                        var userDiary = UserDiary()
                        userDiary.uid = firebaseAuth.currentUser!!.uid
                        userDiary.useId = firebaseAuth.currentUser!!.email
                        userDiary.diary = binding.diary.text.toString()
                        userDiary.date = binding.date.text.toString()
                        firestore?.collection("${firebaseAuth.currentUser!!.email.toString()} diary")?.document(binding.date.text.toString())?.set(userDiary)
                        Toast.makeText(getActivity(),"save diary",Toast.LENGTH_SHORT).show()
                    }
                    binding.savebtn.visibility = View.INVISIBLE
                    binding.editbtn.visibility = View.VISIBLE
                    binding.deletebtn.visibility = View.VISIBLE
                    binding.diaryEditTxt.visibility = View.INVISIBLE
                    binding.diary.visibility = View.VISIBLE
                }
                .addOnFailureListener { exception ->
                    Log.d("ITM", "get failed with ", exception)
                }
        }

        binding.editbtn.setOnClickListener { // 수정 버튼을 누를 시
            binding.diaryEditTxt.visibility = View.VISIBLE
            binding.diary.visibility = View.INVISIBLE
            binding.savebtn.visibility = View.VISIBLE
            binding.editbtn.visibility = View.INVISIBLE
            binding.deletebtn.visibility = View.INVISIBLE
        }

        binding.deletebtn.setOnClickListener {
            binding.diary.visibility = View.INVISIBLE
            binding.diaryEditTxt.setText("")
            binding.diaryEditTxt.visibility = View.VISIBLE
            binding.savebtn.visibility = View.VISIBLE
            binding.editbtn.visibility = View.INVISIBLE
            binding.deletebtn.visibility = View.INVISIBLE
            firestore.collection("${firebaseAuth.currentUser!!.email.toString()} diary")?.document(binding.date.text.toString())?.delete()
            Toast.makeText(getActivity(), "delete diary", Toast.LENGTH_SHORT).show()
        }

        binding.importReceipt.setOnClickListener (({
            val nextScreen = Intent(context, TestActivity::class.java)
            nextScreen.putExtra("category", "household")
            startActivity(nextScreen)
            activity?.finish() }))
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