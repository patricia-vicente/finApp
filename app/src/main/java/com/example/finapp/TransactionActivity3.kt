package com.example.finapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionActivity3(
    private val context: Context,
    private val transaction2ArrayList: ArrayList<TransactionActivity2>
) : RecyclerView.Adapter<TransactionActivity3.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.note.text = transaction2ArrayList[position].note
        holder.amount.text = transaction2ArrayList[position].amount
        holder.date.text = transaction2ArrayList[position].date

    }

    override fun getItemCount(): Int {
        return transaction2ArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var note: TextView = itemView.findViewById(R.id.note_recycler)
        var amount: TextView = itemView.findViewById(R.id.amount_recycler)
        var date: TextView = itemView.findViewById(R.id.date_recycler)
    }
}
