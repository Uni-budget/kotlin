package com.example.babygage_ocr

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.babygage_ocr.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var binding : ActivityLoginBinding
    binding = ActivityLoginBinding.inflate(layoutInflater)

    setContentView(binding.root)


    binding.signupbtn.setOnClickListener {
        val nextScreen = Intent(this, SignupActivity::class.java)
        startActivity(nextScreen)
    }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginbtn.setOnClickListener {
            val email = binding.useremail.text.toString()
            val pass = binding.userpassword.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if(it.isSuccessful) {
                                val nextScreen = Intent(this, IntroselectionActivity::class.java)
                                startActivity(nextScreen)
                        }
                        else {
                            Toast.makeText(this, "check your email(ID) and password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            else {
                Toast.makeText(this,"Empty fields", Toast.LENGTH_SHORT).show()
            }

            //save current data
            Log.d("test","email: ${email}")
            val sharedPref = getSharedPreferences("uj",MODE_PRIVATE)
            //initializing editor
            //initializing editor
            val editor: SharedPreferences.Editor = sharedPref.edit()


            editor.putString("userid", email)
            editor.apply()

         //   val nextScreen = Intent(this, IntroselectionActivity::class.java)
          //  startActivity(nextScreen)

        }
    }
}