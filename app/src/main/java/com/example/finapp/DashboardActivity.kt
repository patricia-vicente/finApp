package com.example.finapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var transactionActivity2ArrayList = ArrayList<TransactionActivity2>()
    private lateinit var transactionActivity3: TransactionActivity3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseStore=FirebaseFirestore.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()
        transactionActivity2ArrayList= ArrayList()
        transactionActivity3= TransactionActivity3(this, transactionActivity2ArrayList)
        binding.recyclerDash.adapter = transactionActivity3
        binding.recyclerDash.layoutManager = LinearLayoutManager(this)
        binding.recyclerDash.setHasFixedSize(true)

        binding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            val intent = when (item.itemId) {
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
        firebaseStore.collection("Expenses").document(firebaseAuth.uid ?: "").collection("Notes")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents ?: listOf()
                    documents.forEach { document ->
                        val transaction = TransactionActivity2(
                            document.getString("id") ?: "",
                            document.getString("note") ?: "",
                            document.getString("amount") ?: "",
                            document.getString("type") ?: "",
                            document.getString("date") ?: ""
                        )
                        transactionActivity2ArrayList.add(transaction)
                    }

                    transactionActivity3 = TransactionActivity3(this@DashboardActivity, transactionActivity2ArrayList)
                    binding.recyclerDash.adapter = transactionActivity3
                } else {
                    // Log error or handle failure
                }
            }
    }
}