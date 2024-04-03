package com.example.finapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finapp.Model.Data
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {

    // Floating Action Buttons
    private lateinit var fMainBtn: FloatingActionButton
    private lateinit var fIncomeBtn: FloatingActionButton
    private lateinit var fExpenseBtn: FloatingActionButton

    // Floating Action Button TextViews
    private lateinit var fIncomeTxt: TextView
    private lateinit var fExpenseTxt: TextView

    private var isOpen = false

    private lateinit var fadeOpen: Animation
    private lateinit var fadeClose: Animation

    //Firebase
    private lateinit var eAuth: FirebaseAuth
    private lateinit var eIncomeDatabase: DatabaseReference
    private lateinit var eExpenseDatabase: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        eAuth = FirebaseAuth.getInstance()
        val currentUser = eAuth.currentUser
        val uid = currentUser?.uid


        val database = Firebase.database

        uid?.let {
            eIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeData").child(it)
            eExpenseDatabase = FirebaseDatabase.getInstance().getReference("ExpenseData").child(it)
        }


        fMainBtn = myView.findViewById(R.id.main_plus_btn)
        fIncomeBtn = myView.findViewById(R.id.income_btn)
        fExpenseBtn = myView.findViewById(R.id.expense_btn)

        fIncomeTxt = myView.findViewById(R.id.income_text)
        fExpenseTxt = myView.findViewById(R.id.expense_text)

        fadeOpen = AnimationUtils.loadAnimation(activity, R.anim.fade_open)
        fadeClose = AnimationUtils.loadAnimation(activity, R.anim.fade_close)

        fMainBtn.setOnClickListener { view ->

            if (isOpen) {
                fIncomeBtn.startAnimation(fadeClose)
                fExpenseBtn.startAnimation(fadeClose)
                fIncomeBtn.isClickable = false
                fExpenseBtn.isClickable = false

                fIncomeTxt.startAnimation(fadeClose)
                fExpenseTxt.startAnimation(fadeClose)
                fIncomeTxt.isClickable = false
                fExpenseTxt.isClickable = false
                isOpen = false
            } else {
                addData()

                fIncomeBtn.startAnimation(fadeOpen)
                fExpenseBtn.startAnimation(fadeOpen)
                fIncomeBtn.isClickable = true
                fExpenseBtn.isClickable = true

                fIncomeTxt.startAnimation(fadeOpen)
                fExpenseTxt.startAnimation(fadeOpen)
                fIncomeTxt.isClickable = true
                fExpenseTxt.isClickable = true
                isOpen = true
            }
        }

        return myView
    }

    private fun addData() {
        fIncomeBtn.setOnClickListener { view ->
            incomeDataInsert()
        }

        fExpenseBtn.setOnClickListener { view ->
            // Adicionar a lógica de manipulação de cliques aqui
        }
    }

    fun incomeDataInsert() {
        val myDialog = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        val myView = inflater.inflate(R.layout.custom_layout, null)
        myDialog.setView(myView)
        val dialog = myDialog.create()

        val editAmount: EditText = myView.findViewById(R.id.amount_edit)
        val editType: EditText = myView.findViewById(R.id.type_edit)
        val editNote: EditText = myView.findViewById(R.id.note_edit)

        val btnSave: Button = myView.findViewById(R.id.save_btn)
        val btnCancel: Button = myView.findViewById(R.id.cancel_btn)

        btnSave.setOnClickListener {
            val type = editType.text.toString().trim()
            val amount = editAmount.text.toString().trim()
            val note = editNote.text.toString().trim()

            if (type.isEmpty()) {
                editType.error = "Field Required"
                return@setOnClickListener
            }

            if (amount.isEmpty()) {
                editAmount.error = "Field Required"
                return@setOnClickListener
            }

            if (note.isEmpty()) {
                editNote.error = "Field Required"
                return@setOnClickListener
            }

            val amountInt = amount.toIntOrNull() ?: 0

            val id = eIncomeDatabase.push().key ?: return@setOnClickListener

            val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val eDate = formatter.format(java.util.Date())

            val data = Data(amountInt, type, note, id, eDate)

            eIncomeDatabase.child(id).setValue(data).addOnSuccessListener {
                Toast.makeText(activity, "Data Added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Toast.makeText(activity, "Failed to add data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
            

            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


    }
}


