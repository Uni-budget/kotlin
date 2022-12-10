package com.example.babygage_ocr

import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babygage_ocr.databinding.ActivityHouseholdMypageBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HouseholdMypageActivity : AppCompatActivity() {

    var mEdtDate: EditText? = null
    var mEdtName: EditText? = null
    var mEdtPrice: EditText? = null
    var myItems = mutableListOf<Items>()
    var myItems2= mutableListOf<Items>()
    var myAdapter = MynumbersAdapter(myItems)
    var btnYearMonthPicker: Button? = null
    var yr = ""
    var month = ""
    var userYearMonth = ""
    var yearmonth = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding : ActivityHouseholdMypageBinding
        binding = ActivityHouseholdMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mEdtDate = binding.editTextDate
        mEdtName = binding.editTextProduct
        mEdtPrice = binding.editTextTextPrice
        val category = intent.getStringExtra("category").toString() // financial or household





        var position = 0
        var receipt_position =0
        var db: MyitemsDB
        db = MyitemsDB.getInstance(this)

        binding.itemList2.adapter = myAdapter
        binding.itemList2.layoutManager = LinearLayoutManager(this)
        // shared preference
        val sharedPref = getSharedPreferences("uj",MODE_PRIVATE)
        //initializing editor
        val editor: SharedPreferences.Editor = sharedPref.edit()
        var userId =  sharedPref.getString("userid", "")

        // Calendar month, year select
        btnYearMonthPicker = binding.btnYearMonthPicker




        // Initiate previous records and display the list of them with recycler view

        var dblist = db.mynumbersDAO().findId(userId!!, category) // Get previous records from database

        val stored = dblist
        for(i in stored){
            myItems.add(i) // add records to myNumbers
        }

        receipt_position = myItems.size
        binding.itemList2.adapter?.notifyDataSetChanged() // NOTIFY recycler view that the list size and items are changed


        var totalList = db.mynumbersDAO().getAll() // Get previous records from database
        var stored2 = totalList
        for(i in stored2){
            myItems2.add(i) // add records to myNumbers
        }
        position = myItems2.size

        // initialize firebase
        val firestore = Firebase.firestore


        // when user types receipt information
        val receive_intent = intent

        val temp = receive_intent.getStringExtra("key01").toString()
        val temp2 = receive_intent.getStringExtra("key02").toString()
        val temp3 = receive_intent.getStringExtra("key03").toString()
        Log.d("test","temp: ${temp}, temp2: ${temp2}, temp3: ${temp3}, category: ${category}")
        if (temp != "" && temp2 != "" && temp3 != ""){
            // make useryearmonth to match with the year, month that user selected
            userYearMonth = temp.substring(0 until 6)
            val item = Items(position,userId!!,category,userYearMonth, temp, temp2, temp3)
            myItems.add(item) // add intended data class with 6 unique random number to the list
            db.mynumbersDAO().insertNumbers(item) // insert it to the database
            val pos = binding.itemList2.adapter?.itemCount?.minus(1) // get last position
            if (pos != null) {
                binding.itemList2.adapter?.notifyItemInserted(pos)// NOTIFY recycler view that new item is inserted
            }

            // add to firebase
            val fire_item = hashMapOf(
                "date" to "${temp}",
                "name" to "${temp2}",
                "price" to "${temp3}",
                "category" to "${category}"
            )

            firestore.collection("Household_${userId}").document("Household_Receipts${receipt_position}")
                .set(fire_item)
                .addOnSuccessListener { Log.d("test", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.d("test", "Error writing document", e) }

            position += 1
            receipt_position += 1

        }

        btnYearMonthPicker!!.setOnClickListener {
            val pd = MyYearMonthPickerDialog()

            pd.setListener(OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                Log.d(
                    "test",
                    "year = $year, month = $monthOfYear, day = $dayOfMonth"
                )
                yr = "$year"
                if ("$monthOfYear".length == 1){
                    month = "0$monthOfYear"
                }else{
                    month = "$monthOfYear"
                }
                yearmonth = "${yr}${month}"
                binding.yearMonth.text = "${yr}/${month}"
                Log.d("test","yearmonth: ${yearmonth}")
                dblist = db.mynumbersDAO().findIdDate(userId!!, yearmonth, category) // Get previous records from database

                myItems.clear()

                val stored3 = dblist
                for(i in stored3){
                    myItems.add(i) // add records to myNumbers
                }
                position = myItems.size
                binding.itemList2.adapter?.notifyDataSetChanged() // NOTIFY recycler view that the list size and items are changed

            })

            pd.show(supportFragmentManager, "YearMonthPickerTest")


        }


        // when 'generate' button is clicked
        binding.generate?.setOnClickListener {
            var date = mEdtDate!!.getText().toString()
            var name = mEdtName!!.getText().toString()
            var price = mEdtPrice!!.getText().toString()

            // make useryearmonth to match with the year, month that user selected
            userYearMonth = date.substring(0 until 6)
            val item = Items(position, userId!!,category,userYearMonth,date, name, price)
            myItems.add(item) // add createdNumber data class with 6 unique random number to the list
            db.mynumbersDAO().insertNumbers(item) // insert it to the database
            val pos = binding.itemList2.adapter?.itemCount?.minus(1) // get last position
            if (pos != null) {
                binding.itemList2.adapter?.notifyItemInserted(pos)// NOTIFY recycler view that new item is inserted
            }

            val fire_item = hashMapOf(
                "date" to "${date}",
                "name" to "${name}",
                "price" to "${price}",
                "category" to "${category}"
            )

            firestore.collection("Household_${userId}").document("Household_Receipts${receipt_position}")
                .set(fire_item)
                .addOnSuccessListener { Log.d("test", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.d("test", "Error writing document", e) }
            // 초기화
            mEdtDate!!.setText("");
            mEdtName!!.setText("");
            mEdtPrice!!.setText("");
            position += 1
            receipt_position += 1

        }


        binding.delete?.setOnClickListener{
            db.mynumbersDAO().deleteIdNumbers(userId, category!!)
            while(binding.itemList2.adapter?.itemCount!! > 0) { // repeat until all items in adapter are deleted
                val pos = binding.itemList2.adapter?.itemCount?.minus(1) // get last position
                if (pos != null) {
                    myItems.removeAt(pos) // delete at the last positions item in list
                    binding.itemList2.adapter?.notifyItemRemoved(pos) // NOTIFY recycler view that item is removed in that position
                    firestore.collection("Household_${userId}")
                        .document("Household_Receipts${pos}").delete();
                }
            }

            totalList = db.mynumbersDAO().getAll() // Get previous records from database
            stored2 = totalList
            for(i in stored2){
                myItems2.add(i) // add records to myNumbers
            }
            position = myItems2.size
            receipt_position = 0


        }

        binding.barchart.setOnClickListener{
            val nextScreen = Intent(this, HouseholdChartActivity::class.java)
            startActivity(nextScreen)
        }

    }
}
