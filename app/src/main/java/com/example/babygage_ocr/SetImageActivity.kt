package com.example.babygage_ocr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.babygage_ocr.databinding.ActivitySetImageBinding


class SetImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivitySetImageBinding
        binding = ActivitySetImageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // 툴바 생성
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

//        val textView = binding.tvImagePath
        val imageView = binding.ivImage

        val imagePath = intent.getStringExtra("path")


        // receive intend name, price, date
        val receive_intent = intent

        val date = receive_intent.getStringExtra("key01")
        val name = receive_intent.getStringExtra("key02")
        val price = receive_intent.getStringExtra("key03")
        val category = receive_intent.getStringExtra("category")

//        textView.text = imagePath
        Log.d("test","set image view: ${imagePath}")
        Glide.with(this).load(imagePath)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)


        binding.dateTxt.setText(date)
        binding.productTxt.setText(name)
        binding.totalTxt.setText(price)

        binding.testBtn.setOnClickListener {
            val nextScreen = Intent(this, TestActivity::class.java)
            startActivity(nextScreen)
        }


        binding.rectangle42.setOnClickListener {

            // 데이터 첨부를하고 액티비티 실행
            val temp: String? = binding.dateTxt.text.toString()
            val temp2: String? = binding.productTxt.text.toString()
            val temp3: String? = binding.totalTxt.text.toString()
            if (category == "financial"){
                val nextScreen = Intent(this, FinancialMypageActivity::class.java)
                nextScreen.putExtra("key01", temp)
                nextScreen.putExtra("key02", temp2)
                nextScreen.putExtra("key03", temp3)
                nextScreen.putExtra("category", category)
                startActivity(nextScreen)

            }else{
                val nextScreen = Intent(this, HouseholdMypageActivity::class.java)
                nextScreen.putExtra("key01", temp)
                nextScreen.putExtra("key02", temp2)
                nextScreen.putExtra("key03", temp3)
                nextScreen.putExtra("category", category)
                startActivity(nextScreen)
            }


        }

    }
}