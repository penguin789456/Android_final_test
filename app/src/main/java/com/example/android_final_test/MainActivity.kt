package com.example.android_final_test

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : AppCompatActivity() {

    private var clickCount = 0
    private lateinit var button: Button
    private lateinit var lineEntries:ArrayList<Entry>
    private lateinit var barEntries:ArrayList<BarEntry>
    private lateinit var myCharLine: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btndetail)
        button.setOnClickListener {
            clickCount++
            updateLineChart()
        }
    }

    private fun updateLineChart() {
        // 將新的條目添加到折線圖中，使用當前點擊次數
        lineEntries.add(Entry(clickCount.toFloat(), clickCount.toFloat()))

        // 創建數據集，如果不存在的話
        var datasetLine: LineDataSet? = myCharLine.data?.getDataSetByIndex(0) as? LineDataSet

        if (datasetLine == null) {
            datasetLine = LineDataSet(lineEntries, "點擊次數")
            datasetLine.color = Color.BLUE
            datasetLine.setDrawCircles(true)

            // 將數據集添加到圖表中
            val lineData = LineData(datasetLine)
            myCharLine.data = lineData
        } else {
            // 更新現有的數據集
            datasetLine.values = lineEntries
        }

        // 刷新圖表
        myCharLine.invalidate()
    }


}