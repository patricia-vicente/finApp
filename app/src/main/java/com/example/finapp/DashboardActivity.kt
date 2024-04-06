package com.example.finapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var transactionActivity2ArrayList = ArrayList<TransactionActivity2>()
    private lateinit var transactionActivity3: TransactionActivity3
    private var sumExpense: Int = 0
    private var sumIncome: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        transactionActivity3 = TransactionActivity3(this, transactionActivity2ArrayList)

        binding.recyclerDash.adapter = transactionActivity3
        binding.recyclerDash.layoutManager = LinearLayoutManager(this)
        binding.recyclerDash.setHasFixedSize(true)

        binding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            val intent = when(item.itemId) {
                R.id.transaction -> Intent(this, TransactionActivity::class.java)
                R.id.income -> Intent(this, IncomeActivity::class.java)
                R.id.expense -> Intent(this, ExpenseActivity::class.java)
                R.id.dashboard -> Intent(this, DashboardActivity::class.java)
                else -> null
            }
            intent?.let {
                startActivity(it)
                true
            } ?: false
        }

        loadData()
    }
    private fun loadData() {
        val userId = firebaseAuth.uid ?: return

        sumExpense = 0
        sumIncome = 0

        firebaseStore.collection("Expenses").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.documents?.forEach { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: 0
                        sumExpense += amount
                    }

                    binding.sumExpense.text = sumExpense.toString()
                    binding.balance.text = (sumIncome - sumExpense).toString()
                }

                transactionActivity3.notifyDataSetChanged()
            }

        firebaseStore.collection("Incomes").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.documents?.forEach { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: 0
                        sumIncome += amount
                    }

                    binding.sumIncome.text = sumIncome.toString()
                    binding.balance.text = (sumIncome - sumExpense).toString()
                }

                transactionActivity3.notifyDataSetChanged()
            }
    }



}
