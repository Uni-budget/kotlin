package com.example.babygage_ocr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.babygage_ocr.databinding.ActivityIntroselectionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class IntroselectionActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityIntroselectionBinding
        binding = ActivityIntroselectionBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        val currentUser = firebaseAuth?.currentUser
        val docRef = firestore.collection("User Account").document(firebaseAuth.currentUser!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                binding.hiOooWelcomeToEasygage.text =
                    "Hi! ${document.data?.get("userName")}. Welcome to Unibudget"
            }

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