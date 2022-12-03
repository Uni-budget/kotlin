package com.example.babygage_ocr

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babygage_ocr.databinding.ActivityFinancialMypageBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FinancialMypageActivity : AppCompatActivity() {
    var mEdtDate: EditText? = null
    var mEdtName: EditText? = null
    var mEdtPrice: EditText? = null
    var myItems = mutableListOf<Items>()
    var myAdapter = MynumbersAdapter(myItems)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityFinancialMypageBinding
        binding = ActivityFinancialMypageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mEdtDate = binding.editTextDate
        mEdtName = binding.editTextProduct
        mEdtPrice = binding.editTextTextPrice

        var position = 0
        var db: MyitemsDB
        db = MyitemsDB.getInstance(this)

        binding.itemList.adapter = myAdapter
        binding.itemList.layoutManager = LinearLayoutManager(this)
        // shared preference
        val sharedPref = getSharedPreferences("uj",MODE_PRIVATE)
        //initializing editor
        val editor: SharedPreferences.Editor = sharedPref.edit()
        var userId =  sharedPref.getString("userid", "")

        // Initiate previous records and display the list of them with recycler view
        val stored = db.mynumbersDAO().findId(userId!!) // Get previous records from database
        for(i in stored){
            myItems.add(i) // add records to myNumbers
        }
        position = myItems.size
        binding.itemList.adapter?.notifyDataSetChanged() // NOTIFY recycler view that the list size and items are changed

        // initialize firebase
        val firestore = Firebase.firestore



        val receive_intent = intent

        val temp = receive_intent.getStringExtra("key01").toString()
        val temp2 = receive_intent.getStringExtra("key02").toString()
        val temp3 = receive_intent.getStringExtra("key03").toString()
        Log.d("test","temp: ${temp}, temp2: ${temp2}, temp3: ${temp3}")
        if (temp != "" || temp2 != "" || temp3 != ""){
            val item = Items(position,userId!!, temp, temp2, temp3)
            myItems.add(item) // add intended data class with 6 unique random number to the list
            db.mynumbersDAO().insertNumbers(item) // insert it to the database
            val pos = binding.itemList.adapter?.itemCount?.minus(1) // get last position
            if (pos != null) {
                binding.itemList.adapter?.notifyItemInserted(pos)// NOTIFY recycler view that new item is inserted
            }

            // add to firebase
            val fire_item = hashMapOf(
                "date" to "${temp}",
                "name" to "${temp2}",
                "price" to "${temp3}"
            )

            firestore.collection("${userId}").document("Receipts${position}")
                .set(fire_item)
                .addOnSuccessListener { Log.d("test", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.d("test", "Error writing document", e) }

            position += 1

        }

//        binding.rowTextResult.setText(temp)
//        binding.rowTextResult2.setText(temp2)
//        binding.rowTextResult3.setText(temp3)

        // when 'generate' button is clicked
        binding.generate?.setOnClickListener {
            var date = mEdtDate!!.getText().toString()
            var name = mEdtName!!.getText().toString()
            var price = mEdtPrice!!.getText().toString()
            val item = Items(position, userId!!,date, name, price)
            myItems.add(item) // add createdNumber data class with 6 unique random number to the list
            db.mynumbersDAO().insertNumbers(item) // insert it to the database
            val pos = binding.itemList.adapter?.itemCount?.minus(1) // get last position
            if (pos != null) {
                binding.itemList.adapter?.notifyItemInserted(pos)// NOTIFY recycler view that new item is inserted
            }

            val fire_item = hashMapOf(
                "date" to "${date}",
                "name" to "${name}",
                "price" to "${price}"
            )

            firestore.collection("${userId}").document("Receipts${position}")
                .set(fire_item)
                .addOnSuccessListener { Log.d("test", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.d("test", "Error writing document", e) }
            // 초기화
            mEdtDate!!.setText("");
            mEdtName!!.setText("");
            mEdtPrice!!.setText("");
            position += 1

        }


        binding.delete?.setOnClickListener{
            db.mynumbersDAO().deleteIdNumbers(userId)
            while(binding.itemList.adapter?.itemCount!! > 0) { // repeat until all items in adapter are deleted
                val pos = binding.itemList.adapter?.itemCount?.minus(1) // get last position
                if (pos != null) {
                    myItems.removeAt(pos) // delete at the last positions item in list
                    binding.itemList.adapter?.notifyItemRemoved(pos) // NOTIFY recycler view that item is removed in that position
                    firestore.collection("${userId}")
                        .document("Receipts${pos}").delete();
                }
            }
            position = 0

        }
        binding.excelbtn.setOnClickListener{
            saveExcel()
        }

    }

 /*
    // 리스트 Excel 저장
    fun onExcelSave(v: View?) {
        // 엑셀에 저장한다.
        saveExcel()
    }
*/

    private fun saveExcel() {
        val workbook: Workbook = HSSFWorkbook()
        val sheet: Sheet = workbook.createSheet() // 새로운 시트 생성
        var row: Row? = sheet.createRow(0) // 새로운 행 생성
        var cell: Cell

        // 1번 셀 생성과 입력
        cell = row!!.createCell(0)
        cell.setCellValue("Date")

        // 2번 셀에 값 생성과 입력
        cell = row!!.createCell(1)
        cell.setCellValue("Product name")

        // 3번 셀에 값 생성과 입력
        cell = row!!.createCell(2)
        cell.setCellValue("Total Price")
        for (i in 0 until myItems.size) { // 데이터 엑셀에 입력
            row = sheet.createRow(i + 1)
            cell = row.createCell(0)
            cell.setCellValue(myItems.get(i).item_date)
            cell = row.createCell(1)
            cell.setCellValue(myItems.get(i).item_name)
            cell = row.createCell(2)
            cell.setCellValue(myItems.get(i).item_price)
        }
        val excelFile = File(getExternalFilesDir(null), "user.xls")
        try {
            val os = FileOutputStream(excelFile)
            workbook.write(os)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(
            applicationContext,
            excelFile.getAbsolutePath() + "에 저장되었습니다",
            Toast.LENGTH_SHORT
        ).show()
    }
}