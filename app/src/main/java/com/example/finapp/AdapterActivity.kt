package com.example.finapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterActivity(
    private val context: Context,
    var modelsActivityArrayList: ArrayList<ModelsActivity>
) : RecyclerView.Adapter<AdapterActivity.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = modelsActivityArrayList[position]
        holder.note.text = transaction.note
        holder.amount.text = transaction.amount
        holder.date.text = transaction.date

        Log.d("TransactionAdapter", "Binding position $position: $transaction")
    }

    override fun getItemCount(): Int {
        return modelsActivityArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var note: TextView = itemView.findViewById(R.id.note_recycler)
        var amount: TextView = itemView.findViewById(R.id.amount_recycler)
        var date: TextView = itemView.findViewById(R.id.date_recycler)
    }
}
