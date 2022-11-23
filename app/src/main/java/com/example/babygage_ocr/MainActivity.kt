package com.example.babygage_ocr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.babygage_ocr.databinding.ActivityMainBinding
import kotlinx.coroutines.selects.select


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var mainFrag:Fragment
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val secondIntent = intent
        val selectVal = secondIntent.getStringExtra("select")
        if (selectVal == "financial" ){
            mainFrag = FinancialmainFragment()

        }else{
           mainFrag = HouseholdmainFragment()

        }
          replaceFragment(mainFrag)


        binding.bottomNavigationview.setOnItemSelectedListener {
            when(it.itemId){
                R.id.finance -> replaceFragment(FinancialmainFragment())
                R.id.house -> replaceFragment(HouseholdmainFragment())
                R.id.mypage -> replaceFragment(MypageFragment())
                R.id.home -> replaceFragment(mainFrag)

        }
            true

        }

      }

    private fun replaceFragment(fragment: Fragment) {
        val fragManager = supportFragmentManager
        val fragmentTransaction = fragManager.beginTransaction()
        fragmentTransaction.replace( R.id.containers, fragment)
        fragmentTransaction.commit()

    }
}
