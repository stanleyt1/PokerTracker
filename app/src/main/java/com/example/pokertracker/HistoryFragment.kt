package com.example.pokertracker

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginBottom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jjoe64.graphview.series.DataPoint
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


class HistoryFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var linearLayoutView: LinearLayout

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
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutView = requireView().findViewById(R.id.linear)

        fetchData()
    }

    private fun fetchData() {
        // fetch sessions
        database.child(auth.currentUser!!.uid).get()
            .addOnSuccessListener {

                // data does not exist
                if (it.value == null) {
                    val empty = TextView(context)
                    empty.text = "No Past Sessions Found"
                    empty.textSize = 18.0f
                    empty.setTextColor(Color.WHITE)
                    linearLayoutView.addView(empty)

                    return@addOnSuccessListener
                }

                var sessions = (it.getValue<HashMap<String, Session>>()!!).values

                val map = sessions.groupBy { it.date }
                var keys = map.keys.toTypedArray()
                keys.sort()

                for (key in keys.reversed()) {

                    val date = TextView(context)
                    date.text = key
                    date.textSize = 18.0f
                    date.setTextColor(Color.WHITE)
                    date.setTypeface(null, Typeface.BOLD)
                    linearLayoutView.addView(date)

                    val space1 = Space(context)
                    space1.minimumHeight = 12
                    linearLayoutView.addView(space1)


                    for (session in map[key]!!) {
                        val view = createSessionView(session, requireContext())
                        linearLayoutView.addView(view)

                        val space2 = Space(context)
                        space2.minimumHeight = 24
                        linearLayoutView.addView(space2)
                    }

                    val space3 = Space(context)
                    space3.minimumHeight = 48
                    linearLayoutView.addView(space3)
                }


            }
            .addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

    }

    private fun createSessionView(session: Session, context: Context): View {

        var constraint = ConstraintLayout(context)
        constraint.id = ConstraintLayout.generateViewId()
        constraint.setBackgroundColor(Color.parseColor("#3A506B"))

        var title = TextView(context)
        title.id = TextView.generateViewId()
        title.text = session.location!!
        title.textSize = 24.0f
        title.setTextColor(Color.WHITE)
        title.setTypeface(null, Typeface.BOLD);

        var profit = TextView(context)
        profit.id = TextView.generateViewId()
        profit.text = String.format("$ %.2f", session.pay_out!! - session.buy_in!!)
        profit.textSize = 24.0f
        profit.setTextColor(Color.WHITE)
        profit.setTypeface(null, Typeface.BOLD);


        constraint.addView(title)
        constraint.addView(profit)
        constraint.setPadding(24, 24, 24, 24)

        var constraintSet = ConstraintSet()
        constraintSet.clone(constraint)
        constraintSet.connect(profit.id, ConstraintSet.END, constraint.id, ConstraintSet.END, 0)

        constraintSet.applyTo(constraint)

        return constraint
    }
}