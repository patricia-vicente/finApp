package com.example.finapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.concurrent.CountDownLatch


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var modelsActivityArrayList = ArrayList<ModelsActivity>()
    private lateinit var adapterActivity: AdapterActivity
    private var sumExpense: Int = 0
    private var sumIncome: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        adapterActivity = AdapterActivity(this, modelsActivityArrayList)

        binding.recyclerDash2.layoutManager = LinearLayoutManager(this)
        binding.recyclerDash2.adapter = adapterActivity
        binding.recyclerDash2.setHasFixedSize(true)

        setNavigationListeners()
        loadData()
    }

    private fun setNavigationListeners() {
        binding.incBtnNav.setOnClickListener {
            navigateTo(IncomeActivity::class.java)
        }

        binding.expBtnNav.setOnClickListener {
            navigateTo(ExpenseActivity::class.java)
        }

        binding.transactionBar.setOnClickListener {
            navigateTo(TransactionActivity::class.java)
        }

        binding.viewDashMain.setOnClickListener {
            navigateTo(DashboardActivity::class.java)
        }

        binding.viewIncMain.setOnClickListener {
            navigateTo(IncomeActivity::class.java)
        }

        binding.viewExpMain.setOnClickListener {
            navigateTo(ExpenseActivity::class.java)
        }

        binding.viewTransMain.setOnClickListener {
            navigateTo(TransactionActivity::class.java)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    private fun loadData() {
        val userId = firebaseAuth.uid ?: return

        modelsActivityArrayList.clear()
        sumExpense = 0
        sumIncome = 0

        val latch = CountDownLatch(2)
        val allTransactions = mutableListOf<ModelsActivity>()

        loadExpenses(userId, latch, allTransactions)
        loadIncomes(userId, latch, allTransactions)

    }

    private fun loadExpenses(userId: String, latch: CountDownLatch, allTransactions: MutableList<ModelsActivity>) {
        firebaseStore.collection("Expenses").document(userId).collection("Notes")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.documents?.forEach { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: 0
                        sumExpense += amount
                        val transaction = ModelsActivity(
                            document.id,
                            document.getString("note") ?: "",
                            amount.toString(),
                            "Expense",
                            document.getString("date") ?: ""
                        )
                        allTransactions.add(transaction)
                    }
                }
                latch.countDown()
            }
    }

    private fun loadIncomes(userId: String, latch: CountDownLatch, allTransactions: MutableList<ModelsActivity>) {
        firebaseStore.collection("Incomes").document(userId).collection("Notes")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.documents?.forEach { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: 0
                        sumIncome += amount
                        val transaction = ModelsActivity(
                            document.id,
                            document.getString("note") ?: "",
                            amount.toString(),
                            "Income",
                            document.getString("date") ?: ""
                        )
                        allTransactions.add(transaction)
                    }
                }
                latch.countDown()
            }
        Thread {
            try {
                latch.await()
                allTransactions.sortByDescending { it.date }
                runOnUiThread {
                    binding.sumExpense.text = sumExpense.toString()
                    binding.sumIncome.text = sumIncome.toString()
                    binding.sumBalance.text = (sumIncome - sumExpense).toString()

                    adapterActivity.modelsActivityArrayList = allTransactions as ArrayList<ModelsActivity>
                    adapterActivity.notifyDataSetChanged()
                }
            } catch (e: InterruptedException) {
                Log.e("DashboardActivity", "Thread interrupted", e)
            }
        }.start()
    }


}
