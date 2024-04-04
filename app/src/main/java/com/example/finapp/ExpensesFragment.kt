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

class ExpensesFragment : Fragment() {

    // Firebase Auth e DatabaseReference
    private lateinit var eAuth: FirebaseAuth
    private lateinit var eExpenseDatabase: DatabaseReference

    // RecyclerView
    private lateinit var recyclerView: RecyclerView

    private lateinit var expenseTotal: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_expenses, container, false)

        eAuth = FirebaseAuth.getInstance()
        val eUser = eAuth.currentUser
        val uid = eUser?.uid ?: ""

        eExpenseDatabase = FirebaseDatabase.getInstance().getReference("ExpenseDatabase").child(uid)

        expenseTotal = myView.findViewById(R.id.expenseTxt)

        recyclerView = myView.findViewById(R.id.expense_recyclerId)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        eExpenseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var expenseSum = 0

                for (mySnapshot in dataSnapshot.children) {
                    val data = mySnapshot.getValue(Data::class.java)
                    expenseSum += data?.amount ?: 0
                }

                val strExpenseSum = expenseSum.toString()
                expenseTotal.text = strExpenseSum
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return myView
    }

    override fun onStart() {
        super.onStart()

        val adapter: FirebaseRecyclerAdapter<Data, MyViewHolder> = object : FirebaseRecyclerAdapter<Data, MyViewHolder>(
            FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(eExpenseDatabase, Data::class.java)
                .build()
        )  {
            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Data) {
                holder.setDate(model.date)
                holder.setType(model.type)
                holder.setNote(model.note)
                holder.setAmount(model.amount)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_expenses, parent, false)
                return MyViewHolder(itemView)
            }

        }

        recyclerView.adapter = adapter
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setDate(date: String) {
            val eDate = itemView.findViewById<TextView>(R.id.date_text_expense)
            eDate.text = date
        }

        fun setType(type: String) {
            val eType = itemView.findViewById<TextView>(R.id.type_text_expense)
            eType.text = type
        }

        fun setNote(note: String) {
            val eNote = itemView.findViewById<TextView>(R.id.note_text_expense)
            eNote.text = note
        }

        fun setAmount(amount: Int) {
            val eAmount = itemView.findViewById<TextView>(R.id.amount_text_expense)
            val strAmount = amount.toString()
            eAmount.text = strAmount
        }
    }
}
