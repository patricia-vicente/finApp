package com.example.finapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IncomeFragment : Fragment() {

    //Firebase database
    private lateinit var eAuth: FirebaseAuth
    private lateinit var eIncomeDatabase: DatabaseReference

    //Recycler View
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myview = inflater.inflate(R.layout.fragment_income, container, false)

        eAuth = FirebaseAuth.getInstance()

        val eUser = eAuth.currentUser
        val uid = eUser?.uid ?: ""

        //storage database
        eIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeData").child(uid)

        //Recycler View
        recyclerView = myview.findViewById(R.id.income_recyclerId)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager


        return myview
    }

    override fun onStart() {
        super.onStart()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var eView: View = itemView

        fun setType(type: String) {
            val eType = eView.findViewById<TextView>(R.id.type_text_income)
            eType.text = type
        }

        fun setNote(note: String) {
            val eNote = eView.findViewById<TextView>(R.id.note_text_income)
            eNote.text = note
        }

        fun setDate(date: String) {
            val eDate = eView.findViewById<TextView>(R.id.date_text_income)
            eDate.text = date
        }

        fun setAmount(amount: String) {
            val eAmount = eView.findViewById<TextView>(R.id.amount_text_income)
            eAmount.text = amount
        }



    }
}

