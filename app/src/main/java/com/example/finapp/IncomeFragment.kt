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

    //text view
    private lateinit var incomeTotal: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val myView = inflater.inflate(R.layout.fragment_income, container, false)

        eAuth = FirebaseAuth.getInstance()

        val eUser = eAuth.currentUser
        val uid = eUser?.uid ?: ""

        //storage database
        eIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeData").child(uid)

        incomeTotal=myView.findViewById(R.id.incomeTxt)

        //recycler view
        recyclerView = myView.findViewById(R.id.income_recyclerId)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager


        eIncomeDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalValue = 0
                for (mysnapshot in dataSnapshot.children) {
                    val data = mysnapshot.getValue(Data::class.java)
                    if (data != null) {
                        totalValue += data.getAmount()
                    }
                }
                val stTotalValue = totalValue.toString()
                // Assuming incomeTotal is a TextView or similar; must run on UI thread if this is UI code
                incomeTotal.setText(stTotalValue)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })


        return myView
    }

    override fun onStart() {

        super.onStart()

        val adapter = object : FirebaseRecyclerAdapter<Data, MyViewHolder>(
            Data::class.java,
            R.layout.recycler_income_data,
            MyViewHolder::class.java,
            eIncomeDatabase
        ) {
            override fun populateViewHolder(viewHolder: MyViewHolder, model: Data, position: Int) {
                viewHolder.setType(model.type)
                viewHolder.setNote(model.note)
                viewHolder.setDate(model.date)
                viewHolder.setAmount(model.amount)
            }
        }

        recyclerView.adapter = adapter
    }

}

private operator fun Int.plusAssign(amount: Unit) {
    TODO("Not yet implemented")
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var eView: View = itemView

        fun setType(type: String) {
            eView.findViewById<TextView>(R.id.type_text_income).text = type
        }

        fun setNote(note: String) {
            eView.findViewById<TextView>(R.id.note_text_income).text = note
        }

        fun setDate(date: String) {
            eView.findViewById<TextView>(R.id.date_text_income).text = date
        }

        fun setAmount(amount: Int) {
            eView.findViewById<TextView>(R.id.amount_text_income).text = amount.toString()
        }
    }



private fun Unit.build() {

}
open class FirebaseRecyclerAdapter<T, U>(
    java: Class<T>,
    recyclerIncomeData: Int,
    java1: Class<U>,
    eIncomeDatabase: DatabaseReference
) {

    open fun populateViewHolder(viewHolder: MyViewHolder, model: Data, position: Int) {}
}






