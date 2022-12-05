package com.example.babygage_ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.babygage_ocr.databinding.ActivityHouseholdChartBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class HouseholdChartActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding : ActivityHouseholdChartBinding
        binding = ActivityHouseholdChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        val currentUser = firebaseAuth?.currentUser
        firestore= FirebaseFirestore.getInstance()

        var today = LocalDate.now()
        Log.d("ITM","today date : " + today.toString().substring(5,7))

        var num = 30
        val entries = ArrayList<BarEntry>()

//
//        entries.add(BarEntry(1.2f, 20.0f))
//        entries.add(BarEntry(2.2f, 70.0f))
//        entries.add(BarEntry(3.2f, 30.0f))
//        entries.add(BarEntry(4.2f, 90.0f))
//        entries.add(BarEntry(5.2f, 70.0f))
//        entries.add(BarEntry(6.2f, 30.0f))
//        entries.add(BarEntry(7.2f, 90.0f))
//        entries.add(BarEntry(8.2f, 90.0f))
//        entries.add(BarEntry(9.2f, 90.0f))
//        entries.add(BarEntry(10.2f, 90.0f))
//        entries.add(BarEntry(11.2f, 90.0f))
//        entries.add(BarEntry(12.2f, 90.0f))
//        entries.add(BarEntry(13.2f, 90.0f))
//        entries.add(BarEntry(14.2f, 90.0f))
//        entries.add(BarEntry(15.2f, 90.0f))
//        entries.add(BarEntry(16.2f, 90.0f))
//        entries.add(BarEntry(17.2f, 90.0f))
//        entries.add(BarEntry(18.2f, 90.0f))
//        entries.add(BarEntry(19.2f, 90.0f))
//        entries.add(BarEntry(20.2f, 90.0f))
//        entries.add(BarEntry(21.2f, 90.0f))
//        entries.add(BarEntry(22.2f, 90.0f))
//        entries.add(BarEntry(23.2f, 90.0f))
//        entries.add(BarEntry(24.2f, 90.0f))
//        entries.add(BarEntry(25.2f, 90.0f))
//        entries.add(BarEntry(26.2f, 90.0f))
//        entries.add(BarEntry(27.2f, 90.0f))
//        entries.add(BarEntry(28.2f, 90.0f))
//        entries.add(BarEntry(29.2f, 90.0f))
//        entries.add(BarEntry(30.2f, 10.0f))
//        entries.add(BarEntry(31.2f, 90.0f))

        var docsize = 0
        firestore.collection("huj0808@naver.com").get()
                //${firebaseAuth.currentUser!!.email.toString()}
            .addOnSuccessListener { snap ->
                Log.d("ITM","size of document : ${snap.size()}")
                docsize = snap.size()
                for(i :Int in 0..docsize-1) {
//                    Log.d("ITM", "in loop")
                    val docRef = firestore.collection("huj0808@naver.com")
                        .document("Receipts${i}")
                    docRef.get()
                        .addOnSuccessListener { document ->
//                            Log.d("ITM", "document data: ${document.data}")
//                            Log.d("ITM","document month : ${document.data?.get("date").toString().substring(4, 6)}")
                            if (document.data?.get("date").toString().substring(4, 6) == today.toString().substring(5,7)) {
                                //today.toString().substring(5,7)
//                                Log.d("ITM","same month")
                                Log.d("ITM", "in this month : ${document.data}")
//                                Log.d("ITM", "document date: ${document.data?.get("date")}")
//                                Log.d("ITM","document month that are same: ${document.data?.get("date").toString().substring(4,6)}")
                                Log.d("ITM","document day : ${document.data?.get("date").toString().substring(6, 8)}")
                                Log.d("ITM","document price : ${document.data?.get("price")}")
//                                Log.d("ITM","${("${document.data?.get("date").toString().substring(6, 8)}"+".2").toFloat()}")
//                                Log.d("ITM",
//                                    document.data?.get("price").toString().toFloat().toString() )

                                entries.add(BarEntry(("${document.data?.get("date").toString().substring(6, 8)}").toFloat(), "${document.data?.get("price")}".toString().toFloat()))
                                Log.d("ITM", entries.toString())
                                var set = BarDataSet(entries,"DataSet") // 데이터셋 초기화
                                Log.d("ITM", set.toString())
//        set.color = ContextCompat.getColor(applicationContext!!,R.color.design_default_color_primary_dark) // 바 그래프 색 설정

                                val dataSet :ArrayList<IBarDataSet> = ArrayList()
                                dataSet.add(set)
                                val data = BarData(dataSet)
                                data.barWidth = 0.5f //막대 너비 설정
                                binding.barchart.run {
                                    this.data = data //차트의 데이터를 data로 설정해줌.
                                    setFitBars(true)
                                    invalidate()
                                }
                            }
                            else {
                                Log.d("ITM", "not in this month : ${document.data}")
//                                entries.add(BarEntry(("${document.data?.get("date").toString().substring(6, 8)}"+".2").toFloat(),0.0f))
//                                Log.d("ITM", entries.toString())
                            }
                        }
                }
                Log.d("ITM","out loop")
            }
            .addOnFailureListener { exception ->
                Log.d("ITM", "get failed with ", exception) }



        binding.barchart.run {
            description.isEnabled = false //차트 옆에 별도로 표기되는 description이다. false로 설정하여 안보이게 했다.
            setMaxVisibleValueCount(31) // 최대 보이는 그래프 개수를 31개로 정해주었다.
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false)//그래프의 그림자
            setDrawGridBackground(false)//격자구조 넣을건지
            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = 1000000f //100 위치에 선을 그리기 위해 101f로 맥시멈을 정해주었다
                axisMinimum = 0f // 최소값 0
                granularity = 50f // 50 단위마다 선을 그리려고 granularity 설정 해 주었다.
                //위 설정이 20f였다면 총 5개의 선이 그려졌을 것
                setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
//                axisLineColor = ContextCompat.getColor(context, R.color.colorAxis) // 축 색깔 설정
//                gridColor = ContextCompat.getColor(context, R.color.colorAxis) // 축 아닌 격자 색깔 설정
//                textColor =
//                    ContextCompat.getColor(context, R.color.colorSemi50Black) // 라벨 텍스트 컬러 설정
                textSize = 10f //라벨 텍스트 크기
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
//                textColor = ContextCompat.getColor(context, R.color.colorSemi70Black) //라벨 색상
                valueFormatter = MyXAxisFormatter() // 축 라벨 값 바꿔주기 위함
                textSize = 9f // 텍스트 크기
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정

        }
    }
    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
}