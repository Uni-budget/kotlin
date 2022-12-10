package com.example.babygage_ocr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.babygage_ocr.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MypageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MypageFragment : Fragment(){

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    // TODO: Rename and change types of parameters
    lateinit var binding: FragmentMypageBinding
    private var param1: String? = null
    private var param2: String? = null
    var userName:String = ""
    var email:String = ""

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
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        setUpView()

        // Inflate the layout for this fragment
        return binding.root
    }




    private fun setUpView() {

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth?.currentUser

        val docRef = firestore.collection("User Account")
            .document(firebaseAuth.currentUser!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                binding.userName.text = "${document.data?.get("userName")}"
                binding.userEmail.text = "${document.data?.get("userId")}"
            }

        binding.householdExpenditure.visibility = View.INVISIBLE
        binding.householdTotal.visibility = View.INVISIBLE
        binding.financialExpenditure.visibility = View.INVISIBLE
        binding.financialTotal.visibility = View.INVISIBLE

        var sum_household = 0
        var sum_financial = 0

        var today = LocalDate.now()
        Log.d("ITM", "today : $today")
//        binding.householdExpenditure.text = "${sum_household}원"
//        binding.financialExpenditure.text = "${sum_financial}원"
//        firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}").get()
//            //${firebaseAuth.currentUser!!.email.toString()}
//            .addOnSuccessListener { snap ->
//                Log.d("ITM", "size of document : ${snap.size()}")
//                docsize = snap.size()
//                for (i: Int in 0..docsize - 1) {
//                    val receiptRef =
//                        firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}")
//                            .document("Household_Receipts${i}")
//                    receiptRef.get()
//                        .addOnSuccessListener { document ->
//                            Log.d("ITM", "document date = ${document.data?.get("date")}")
//                            if (document.data?.get("date").toString()
//                                    .substring(4, 6) == today.toString().substring(5, 7)
//                            ) {
//                                sum_household =
//                                    sum_household + document.data?.get("price").toString().toInt()
//                                Log.d("ITM", sum_household.toString())
//                                Log.d("ITM", binding.householdExpenditure.text.toString())
//                            } else {
//                                Log.d("ITM", "not on that date")
//                            }
//                            binding.householdExpenditure.text = "${sum_household}원"
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("ITM", "get failed with ", exception)
//            }
//
//        firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}").get()
//            //${firebaseAuth.currentUser!!.email.toString()}
//            .addOnSuccessListener { snap ->
//                Log.d("ITM", "size of document : ${snap.size()}")
//                docsize = snap.size()
//                for (i: Int in 0..docsize - 1) {
//                    val receiptRef =
//                        firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}")
//                            .document("Financial_Receipts${i}")
//                    receiptRef.get()
//                        .addOnSuccessListener { document ->
//                            Log.d("ITM", "document date = ${document.data?.get("date")}")
//                            Log.d("ITM", "substring month = ${document.data?.get("date").toString().substring(4, 6)}")
//                            Log.d("ITM", "today month = ${today.toString().substring(5, 7)}")
//
//                            if (document.data?.get("date").toString()
//                                    .substring(4, 6) == today.toString().substring(5, 7)) {
//                                sum_financial = sum_financial + document.data?.get("price").toString().toInt()
//                                Log.d("ITM", sum_financial.toString())
//                                Log.d("ITM", binding.financialExpenditure.text.toString())
//                            } else {
//                                Log.d("ITM", "not on that date")
//                            }
//                            binding.financialExpenditure.text = "${sum_financial}원"
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("ITM", "get failed with ", exception) }



        binding.btnMyFinancial?.setOnClickListener (({
            val nextScreen = Intent(context, FinancialMypageActivity::class.java)

            nextScreen.putExtra("key01", "")
            nextScreen.putExtra("key02", "")
            nextScreen.putExtra("key03", "")
            nextScreen.putExtra("category", "financial")


            startActivity(nextScreen)
            activity?.finish()
        })
        )

        binding.btnMyHouse?.setOnClickListener (({
            val nextScreen = Intent(context, HouseholdMypageActivity::class.java)

            nextScreen.putExtra("key01", "")
            nextScreen.putExtra("key02", "")
            nextScreen.putExtra("key03", "")
            nextScreen.putExtra("category", "household")



            startActivity(nextScreen)
            activity?.finish()
        })
        )

        binding.button1.setOnClickListener {
            Toast.makeText(activity, "Household expenditure", Toast.LENGTH_SHORT).show()
            Log.d("ITM", "Household expenditure")
            sum_household = 0
            var docsize = 0

            var today = LocalDate.now()
            Log.d("ITM", "today : $today")
            binding.householdExpenditure.text = "${sum_household}원"
            binding.financialExpenditure.text = "${sum_financial}원"
            firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}").get()
                //${firebaseAuth.currentUser!!.email.toString()}
                .addOnSuccessListener { snap ->
                    Log.d("ITM", "size of document : ${snap.size()}")
                    docsize = snap.size()
                    for (i: Int in 0..docsize - 1) {
                        val receiptRef =
                            firestore.collection("Household_${firebaseAuth.currentUser!!.email.toString()}")
                                .document("Household_Receipts${i}")
                        receiptRef.get()
                            .addOnSuccessListener { document ->
                                Log.d("ITM", "document date = ${document.data?.get("date")}")
                                if (document.data?.get("date").toString()
                                        .substring(4, 6) == today.toString().substring(5, 7)
                                ) {
                                    sum_household =
                                        sum_household + document.data?.get("price").toString().toInt()
                                    Log.d("ITM", sum_household.toString())
                                    Log.d("ITM", binding.householdExpenditure.text.toString())
                                } else {
                                    Log.d("ITM", "not on that date")
                                }
                                binding.householdExpenditure.text = "${sum_household}원"
                                binding.householdExpenditure.visibility = View.VISIBLE
                                binding.householdTotal.visibility = View.VISIBLE
                                binding.financialExpenditure.visibility = View.INVISIBLE
                                binding.financialTotal.visibility = View.INVISIBLE
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ITM", "get failed with ", exception)
                }
        }

        binding.button2.setOnClickListener {
            Toast.makeText(activity,"Financial Audit expenditure", Toast.LENGTH_SHORT).show()
            Log.d("ITM","Financial Audit expenditure")
            sum_financial = 0
            var docsize = 0
            firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}").get()
                //${firebaseAuth.currentUser!!.email.toString()}
                .addOnSuccessListener { snap ->
                    Log.d("ITM", "size of document : ${snap.size()}")
                    docsize = snap.size()
                    for (i: Int in 0..docsize - 1) {
                        val receiptRef =
                            firestore.collection("Financial_${firebaseAuth.currentUser!!.email.toString()}")
                                .document("Financial_Receipts${i}")
                        receiptRef.get()
                            .addOnSuccessListener { document ->
                                Log.d("ITM", "document date = ${document.data?.get("date")}")
                                Log.d("ITM", "substring month = ${document.data?.get("date").toString().substring(4, 6)}")
                                Log.d("ITM", "today month = ${today.toString().substring(5, 7)}")

                                if (document.data?.get("date").toString()
                                        .substring(4, 6) == today.toString().substring(5, 7)) {
                                    sum_financial = sum_financial + document.data?.get("price").toString().toInt()
                                    Log.d("ITM", sum_financial.toString())
                                    Log.d("ITM", binding.financialExpenditure.text.toString())
                                } else {
                                    Log.d("ITM", "not on that date")
                                }
                                binding.financialExpenditure.text = "${sum_financial}원"
                                binding.householdExpenditure.visibility = View.INVISIBLE
                                binding.householdTotal.visibility = View.INVISIBLE
                                binding.financialExpenditure.visibility = View.VISIBLE
                                binding.financialTotal.visibility = View.VISIBLE
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ITM", "get failed with ", exception) }
        }
    }



//        // receive intend name, price, date
//        val receive_intent = getActivity()?.intent
//
//        userName = receive_intent?.getStringExtra("sendname").toString()
//        email = receive_intent?.getStringExtra("sendemail").toString()
//
//        binding.userName.text = userName
//        binding.userEmail.text = email



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MypageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}