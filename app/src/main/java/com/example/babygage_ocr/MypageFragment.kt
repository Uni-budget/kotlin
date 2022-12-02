package com.example.babygage_ocr

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.example.babygage_ocr.databinding.FragmentMypageBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*


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

        binding.btnMyFinancial.setOnClickListener (({
            val nextScreen = Intent(context, FinancialMypageActivity::class.java)
            startActivity(nextScreen)
            activity?.finish()
        })
        )

        binding.btnMyHouse.setOnClickListener (({
            val nextScreen = Intent(context, HouseholdMypageActivity::class.java)
            startActivity(nextScreen)
            activity?.finish()
        })
        )

        binding.toggleButton.addOnButtonCheckedListener{ toggleButton, checkedId, isChecked ->
            if(isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        Toast.makeText(activity,"Household expenditure", Toast.LENGTH_SHORT).show()
                        }
                    R.id.button2 -> {
                        Toast.makeText(activity,"Financial Audit expenditure", Toast.LENGTH_SHORT).show()
                         }
                }
            }
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