package com.example.finapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finapp.Model.Data
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IncomeFragment : Fragment() {

    //Firebase database
    private lateinit var eAuth: FirebaseAuth
    private lateinit var eIncomeDatabase: DatabaseReference

    //recycler view
    private lateinit var recyclerView: RecyclerView

    private lateinit var incomeTotal: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_income, container, false)

        eAuth = FirebaseAuth.getInstance()
        val eUser = eAuth.currentUser
        val uid = eUser?.uid ?: ""

        //storage database
        eIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeData").child(uid)

        incomeTotal=myView.findViewById(R.id.incomeTxt)

        recyclerView = myView.findViewById(R.id.income_recyclerId)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager


        eIncomeDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var incomeSum = 0

                for (mySnapshot in dataSnapshot.children) {
                    val data = mySnapshot.getValue(Data::class.java)
                    incomeSum += data?.amount ?: 0
                }

                val strIncomeSum = incomeSum.toString()
                incomeTotal.text = strIncomeSum
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return myView
    }

    override fun onStart() {
        super.onStart()

        val adapter: FirebaseRecyclerAdapter<Data, ExpensesFragment.MyViewHolder> = object : FirebaseRecyclerAdapter<Data, ExpensesFragment.MyViewHolder>(
            FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(eIncomeDatabase, Data::class.java)
                .build()
        )  {
            override fun onBindViewHolder(holder: ExpensesFragment.MyViewHolder, position: Int, model: Data) {
                holder.setDate(model.date)
                holder.setType(model.type)
                holder.setNote(model.note)
                holder.setAmount(model.amount)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesFragment.MyViewHolder {
                TODO("Not yet implemented")
            }

        }

        recyclerView.adapter = adapter
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setDate(date: String) {
            val eDate = itemView.findViewById<TextView>(R.id.date_text_income)
            eDate.text = date
        }

        fun setType(type: String) {
            val eType = itemView.findViewById<TextView>(R.id.type_text_income)
            eType.text = type
        }

        fun setNote(note: String) {
            val eNote = itemView.findViewById<TextView>(R.id.note_text_income)
            eNote.text = note
        }

        fun setAmount(amount: Int) {
            val eAmount = itemView.findViewById<TextView>(R.id.amount_text_income)
            val strAmount = amount.toString()
            eAmount.text = strAmount
        }
    }

}










