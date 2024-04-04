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
        val uid = eAuth.currentUser?.uid ?: ""

        eExpenseDatabase = FirebaseDatabase.getInstance().getReference("ExpenseDatabase").child(uid)

        expenseTotal = myView.findViewById(R.id.expenseTxt)
        recyclerView = myView.findViewById(R.id.expense_recyclerId)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        eExpenseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var expenseSum = 0
                dataSnapshot.children.forEach { mySnapshot ->
                    val data = mySnapshot.getValue(Data::class.java)
                    expenseSum += data?.amount ?: 0
                }
                expenseTotal.text = expenseSum.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return myView
    }

    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Data>()
            .setQuery(eExpenseDatabase, Data::class.java)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Data) {
                holder.setDate(model.date ?: "")
                holder.setType(model.type ?: "")
                holder.setNote(model.note ?: "")
                holder.setAmount(model.amount)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_expense_data, parent, false)
                return MyViewHolder(itemView)
            }
        }

        recyclerView.adapter = adapter
        adapter.startListening()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setDate(date: String) {
            itemView.findViewById<TextView>(R.id.date_text_expense).text = date
        }

        fun setType(type: String) {
            itemView.findViewById<TextView>(R.id.type_text_expense).text = type
        }

        fun setNote(note: String) {
            itemView.findViewById<TextView>(R.id.note_text_expense).text = note
        }

        fun setAmount(amount: Int) {
            itemView.findViewById<TextView>(R.id.amount_text_expense).text = amount.toString()
        }
    }


}



