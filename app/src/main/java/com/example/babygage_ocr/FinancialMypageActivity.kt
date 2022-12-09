package com.example.babygage_ocr

import android.annotation.SuppressLint
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.babygage_ocr.databinding.ActivityFinancialMypageBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FinancialMypageActivity : AppCompatActivity() {
    var mEdtDate: EditText? = null
    var mEdtName: EditText? = null
    var mEdtPrice: EditText? = null
    var myItems = mutableListOf<Items>()
    var myAdapter = MynumbersAdapter(myItems)
    var btnYearMonthPicker: Button? = null
    var yr = ""
    var month = ""
    var userYearMonth = ""
    var yearmonth = ""


//    var d =
//        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            Log.d(
//                "test",
//                "year = $year, month = $monthOfYear, day = $dayOfMonth"
//            )
//            yr = "$year"
//            if ("$monthOfYear".length == 1){
//                month = "0$monthOfYear"
//            }else{
//                month = "$monthOfYear"
//            }
//            yearmonth = "${yr}${month}"
//        }

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

        // Calendar month, year select
        btnYearMonthPicker = binding.btnYearMonthPicker




        // Initiate previous records and display the list of them with recycler view

        var dblist = db.mynumbersDAO().findId(userId!!) // Get previous records from database

        val stored = dblist
        for(i in stored){
            myItems.add(i) // add records to myNumbers
        }
        position = myItems.size
        binding.itemList.adapter?.notifyDataSetChanged() // NOTIFY recycler view that the list size and items are changed

        // initialize firebase
        val firestore = Firebase.firestore






// when user types receipt information
        val receive_intent = intent

        val temp = receive_intent.getStringExtra("key01").toString()
        val temp2 = receive_intent.getStringExtra("key02").toString()
        val temp3 = receive_intent.getStringExtra("key03").toString()
        Log.d("test","temp: ${temp}, temp2: ${temp2}, temp3: ${temp3}")
        if (temp != "" && temp2 != "" && temp3 != ""){
            // make useryearmonth to match with the year, month that user selected
            Log.d("ITM","temp : $temp")
            userYearMonth = temp.substring(0 until 6)
            val item = Items(position,userId!!,userYearMonth, temp, temp2, temp3)
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
                dblist = db.mynumbersDAO().findIdDate(userId!!, yearmonth) // Get previous records from database

                myItems.clear()

                val stored2 = dblist
                for(i in stored2){
                    myItems.add(i) // add records to myNumbers
                }
                position = myItems.size
                binding.itemList.adapter?.notifyDataSetChanged() // NOTIFY recycler view that the list size and items are changed

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
            val item = Items(position, userId!!,userYearMonth,date, name, price)
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
//            saveExcel()
            exportExcelData()

        }

        binding.barchart.setOnClickListener {
            val nextScreen = Intent(this, FinancialChartActivity::class.java)
            startActivity(nextScreen)
        }
    }

    /*
       // 리스트 Excel 저장
       fun onExcelSave(v: View?) {
           // 엑셀에 저장한다.
           saveExcel()
       }
   */

    @SuppressLint("SimpleDateFormat")
    fun exportExcelData() {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
        val formatDate = sdfNow.format(date)

        saf("FinancialAudit_${formatDate}.xls")
    }

    private fun saf(fileName: String?) {
        try {
            /**
             * SAF 파일 편집
             */
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_TITLE, fileName)
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            startActivityForResult.launch(intent)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private var startActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                exportExcel(uri)
            }
        }
    }


    //Export
    private var pfd: ParcelFileDescriptor? = null
    private var fileOutputStream: FileOutputStream? = null

    private fun exportExcel(uri: Uri) = CoroutineScope(Dispatchers.IO).launch {


        val wb: Workbook = HSSFWorkbook()
        val sheet: Sheet = wb.createSheet()

        val now = System.currentTimeMillis()
        val formatDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(now))


        var row: Row = sheet.createRow(0) // 새로운 행 생성
        var cell: Cell
        Log.d("test", "Inside saveExcel()")

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


//        val dayRow: Row = sheet.createRow(0)
//        dayRow.createCell(0).setCellValue(formatDate)

        try {
            pfd = contentResolver.openFileDescriptor(uri, "w")
            fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
            wb.write(fileOutputStream)
        } catch (e: Exception) {
            // Util.showNotification("error: ${e.message}", "error")
        } finally {
            try {
                wb.close()
                fileOutputStream?.close()
                pfd?.close()
                //Util.showNotification("저장 되었습니다.", "success")

            } catch (e: Exception) {
                e.printStackTrace()
                //Util.showNotification("error: ${e.message}", "error")
            }

        }


        Toast.makeText(
            applicationContext,
            "Excel file successfully saved!",
            Toast.LENGTH_SHORT
        ).show()
    }









    private fun saveExcel() {
        val workbook = HSSFWorkbook()
        val sheet: HSSFSheet = workbook.createSheet("Sheet1") // 새로운 시트 생성
        var row: HSSFRow = sheet.createRow(0) // 새로운 행 생성
        var cell: HSSFCell
        Log.d("test", "Inside saveExcel()")

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

        // 일반 파일 폴더
        // 일반 파일 폴더
        val fileFileName = getFileStreamPath("Android")
        val getFileName = fileFileName.path

        val excelFile = File(getFileName, "user.xls")

        try {
            val os = FileOutputStream(excelFile)
            workbook.write(os)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        Toast.makeText(
            applicationContext,
            excelFile.getAbsolutePath() + "에 저장되었습니다",
            Toast.LENGTH_SHORT
        ).show()
    }
}
