package com.example.babygage_ocr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.babygage_ocr.databinding.FragmentFinancialmainBinding
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

/**
 * A simple [Fragment] subclass.
 * Use the [FinancialmainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinancialmainFragment : Fragment() {
    lateinit var binding: FragmentFinancialmainBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

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
        binding = FragmentFinancialmainBinding.inflate(inflater, container, false)
        setUpView()


        // Inflate the layout for this fragment
        return binding.root
    }
    private fun setUpView() {

        firebaseAuth= FirebaseAuth.getInstance()
        val currentUser = firebaseAuth?.currentUser
        firestore= FirebaseFirestore.getInstance()

//        binding.date.bringToFront()
        //날짜 형태
        val dateform : DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        //date type (오늘 날짜)
        val date : Date = Date(binding.calendarView.date)
        var today = LocalDate.now()

        binding.date.text = dateform.format(date)

        // total expenditure
        var docsize = 0
        var sum = 0
        binding.expenditure.bringToFront()
        firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}").get()
            //${firebaseAuth.currentUser!!.email.toString()}
            .addOnSuccessListener { snap ->
                Log.d("ITM", "size of document : ${snap.size()}")
                docsize = snap.size()
                for (i: Int in 0..docsize-1) {
                    val receiptRef =
                        firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}")
                            .document("Financial_Receipts${i}")
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

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            var day : String = "${year}년 ${month+1}월 ${dayOfMonth}일"
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
            firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}").get()
                //${firebaseAuth.currentUser!!.email.toString()}
                .addOnSuccessListener { snap ->
                    Log.d("ITM", "size of document : ${snap.size()}")
                    docsize = snap.size()
                    for (i: Int in 0..docsize-1) {
                        val receiptRef =
                            firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}")
                                .document("Financial_Receipts${i}")
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
        }
        binding.takePictureBtn.setOnClickListener (({
            val nextScreen = Intent(context, TestActivity::class.java)
            nextScreen.putExtra("category", "financial")
            startActivity(nextScreen)
            activity?.finish()
        })
        )

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FinancialmainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FinancialmainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}