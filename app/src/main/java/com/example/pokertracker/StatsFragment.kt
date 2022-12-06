package com.example.pokertracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatsFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var logOutButton: TextView

    private lateinit var graph: GraphView

    private var totalBalance = 0.0
    private var winRate = 0.0
    private var avgWin = 0.0
    private var std = 0.0
    private var variance = 0.0
    private var series: Array<DataPoint> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth  = Firebase.auth
        database =  Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // gets latest stats from database & updates UI
        fetchData()

        graph = requireView().findViewById(R.id.graph)

        // logs out a user
        logOutButton = requireView().findViewById(R.id.logout)
        logOutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    fun updateUI() {
    // update UI
        requireView().findViewById<TextView>(R.id.editTotalBalance).text = String.format("$ %.2f", totalBalance)
        requireView().findViewById<TextView>(R.id.editWinRate).text = "${winRate} %"
        requireView().findViewById<TextView>(R.id.editAvgWin).text = String.format("$ %.2f", avgWin)
        requireView().findViewById<TextView>(R.id.editStdDeviation).text = "${std}"
        requireView().findViewById<TextView>(R.id.editVariance).text = "${variance}"

        graph.animate()
        graph.gridLabelRenderer.horizontalAxisTitle = "Time"
        graph.gridLabelRenderer.verticalAxisTitle= "Profit"
        graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        graph.addSeries(LineGraphSeries(series))

    }

    fun fetchData() {

        var sessions: MutableCollection<Session>

        // fetch sessions
        database.child(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                sessions = (it.getValue<HashMap<String, Session>>()!!).values

                var handsPlayed = 0
                var handsWon = 0
                val formatter = SimpleDateFormat("dd/MM/yyyy")

                for (session in sessions) {
                    Log.i("elo", "$session")
                    totalBalance += session.pay_out!! - session.buy_in!!
                    handsPlayed += session.hands_played!!
                    handsWon += session.hands_won!!

                    val date = formatter.parse(session.date!!)
                    Log.i("elo", "$date")
                    series += DataPoint(date, totalBalance)
                }

                winRate = (handsWon.toDouble() / handsPlayed * 100).roundToInt() / 100.0
                avgWin = ((totalBalance / handsPlayed) * 100).roundToInt() / 100.0

                var meanSquaredDiff = 0.0
                for (session in sessions) {
                    meanSquaredDiff += ((session.pay_out!! - session.buy_in!!) - avgWin).pow(2.0)
                }
                variance = ((meanSquaredDiff / sessions.size )* 100).roundToInt() / 100.0
                std = (sqrt(meanSquaredDiff / sessions.size) * 100).roundToInt() / 100.0

                series.sortWith(Comparator {a: DataPoint, b:DataPoint ->
                    a.x.compareTo(b.x)
                })

                updateUI()

            }
            .addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }


    }


}