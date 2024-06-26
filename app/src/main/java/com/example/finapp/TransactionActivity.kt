package com.example.finapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.databinding.ActivityTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var transactionOp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseStore=FirebaseFirestore.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()
        firebaseUser=firebaseAuth.currentUser


        binding.incBtnNav.setOnClickListener {
            try {
                val intent = Intent(this@TransactionActivity, IncomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.expBtnNav.setOnClickListener {
            try {
                val intent = Intent(this@TransactionActivity, ExpenseActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.transactionBar.setOnClickListener {
            try {
                val intent = Intent(this@TransactionActivity, TransactionActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }



        binding.incomeBtn.setOnClickListener {
            transactionOp="Income"
            val amount = binding.amountEdit.text.toString().trim()
            val type = binding.typeEdit.text.toString().trim()
            val note = binding.noteEdit.text.toString().trim()

            if (amount.isEmpty()) {
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault())
            val currentDateAndTime = sdf.format(java.util.Date())

            val id = UUID.randomUUID().toString()
            val transaction = hashMapOf<String, Any>(
                "id" to id,
                "amount" to amount,
                "note" to note,
                "type" to type,
                "date" to currentDateAndTime
            )

            firebaseStore.collection("Incomes").document(firebaseAuth.uid.toString()).collection("Notes").document(id)
                .set(transaction)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                    binding.amountEdit.text.clear()
                    binding.noteEdit.text.clear()
                    binding.typeEdit.text.clear()

                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                }

        }

        binding.expenseBtn.setOnClickListener {
            transactionOp="Expenses"
            val amount = binding.amountEdit.text.toString().trim()
            val type = binding.typeEdit.text.toString().trim()
            val note = binding.noteEdit.text.toString().trim()

            if (amount.isEmpty()) {
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault())
            val currentDateAndTime = sdf.format(java.util.Date())

            val id = UUID.randomUUID().toString()
            val transaction = hashMapOf<String, Any>(
                "id" to id,
                "amount" to amount,
                "note" to note,
                "type" to type,
                "date" to currentDateAndTime
            )

            firebaseStore.collection("Expenses").document(firebaseAuth.uid.toString()).collection("Notes").document(id)
                .set(transaction)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                    binding.amountEdit.text.clear()
                    binding.noteEdit.text.clear()
                    binding.typeEdit.text.clear()

                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                }






        }

        }


    }
