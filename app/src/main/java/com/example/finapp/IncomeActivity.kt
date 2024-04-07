package com.example.finapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityIncomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class IncomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomeBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var transactionActivity2ArrayList = ArrayList<TransactionActivity2>()
    private lateinit var transactionActivity3: TransactionActivity3
    private var sumExpense: Int = 0
    private var sumIncome: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        transactionActivity3 = TransactionActivity3(this, transactionActivity2ArrayList)

        binding.recyclerDashIncome.layoutManager = LinearLayoutManager(this)
        binding.recyclerDashIncome.adapter =
            TransactionActivity3(this, transactionActivity2ArrayList)
        binding.recyclerDashIncome.setHasFixedSize(true)

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
        binding.btnRefreshIncome.setOnClickListener {
            try {
                val intent = Intent(this@IncomeActivity, IncomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }


        loadData()
    }

    private fun loadData() {
        val userId = firebaseAuth.uid ?: return

        transactionActivity2ArrayList.clear() // Limpa a lista para garantir que está vazia antes de carregar novos dados.

        firebaseStore.collection("Incomes").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.documents?.forEach { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: 0

                        val transaction = TransactionActivity2(
                            document.id,
                            document.getString("note") ?: "",
                            amount.toString(),
                            "Income",
                            document.getString("date") ?: ""
                        )
                        transactionActivity2ArrayList.add(transaction)
                    }
                    transactionActivity3.notifyDataSetChanged() // Notifica o adapter de que os dados foram atualizados.
                }
            }
    }

}
