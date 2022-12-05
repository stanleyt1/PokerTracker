package com.example.pokertracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.pokertracker.databinding.ActivityHomeScreenBinding
import com.google.android.gms.fitness.data.DataPoint
import com.google.firebase.auth.FirebaseAuth
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


//may need onResume()
/*
1)Need to calculate values for:
    Total Balance gained
    Win Rate
    Avg. Win amount
    Std. Deviation
    Variance

2)Need to draw graph with
    x=time(Maybe based on the last 30 entries or something)
    y=Total Balance gained

3)Make sure to pull user name from email and add to screen

 */
class HomeScreen : AppCompatActivity(){
    private lateinit var binding: ActivityHomeScreenBinding

    private lateinit var graphView: GraphView

    private var winRate: Float=0F
    private var avgWin: Float=0F
    private var stdDev: Float=0F
    private var variance: Float=0F
    private var totalBalance: Float=0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayCounts()
        graphView = findViewById(R.id.graph)
        drawGraph()
    }
    //may need onRestart, onStart, and onResume
    private fun displayCounts() {
        binding.editWinRate.text="$winRate %"
        binding.editAvgWin.text= "$ ${String.format("%.2f", avgWin)}"
        binding.editStdDeviation.text="$stdDev"
        binding.editVariance.text="$variance"
        binding.editTotalBalance.text="$ ${String.format("%.2f", totalBalance)}"
    }

    private fun drawGraph(){
        //clearing graph
        graphView.removeAllSeries()

        //These are test points until it is linked up with Database to pull info
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
            // on below line we are adding
            // each point on our x and y axis.
            DataPoint(0.0, 1.0),
            DataPoint(1.0, 3.0),
            DataPoint(2.0, 4.0),
            DataPoint(3.0, 9.0),
            DataPoint(4.0, 6.0),
            DataPoint(5.0, 3.0),
            DataPoint(6.0, 6.0),
            DataPoint(7.0, 1.0),
            DataPoint(8.0, 2.0)
            )
        )
        graphView.setTitle("Total Balance over Time")

        // on below line adding animation
        graphView.animate()

        // on below line we are setting scalable.
        graphView.viewport.isScalable = true

        // on below line we are setting scalable y
        graphView.viewport.setScalableY(true)

        // on below line we are setting color for series.
        series.color = R.color.purple_200

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series)
    }
}