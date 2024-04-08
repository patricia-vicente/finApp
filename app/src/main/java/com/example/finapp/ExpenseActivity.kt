package com.example.finapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityExpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class ExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var modelsActivityArrayList = ArrayList<ModelsActivity>()
    private lateinit var adapterActivity: AdapterActivity
    private var sumExpense: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        adapterActivity = AdapterActivity(this, modelsActivityArrayList)

        binding.recyclerDashExpense.layoutManager = LinearLayoutManager(this)
        binding.recyclerDashExpense.adapter = AdapterActivity(this, modelsActivityArrayList)
        binding.recyclerDashExpense.setHasFixedSize(true)


        binding.incBtnNav.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, IncomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.expBtnNav.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, ExpenseActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.transactionBar.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, TransactionActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.viewIncDashMain.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.incExp.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, IncomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.expExp.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, ExpenseActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.transExp.setOnClickListener {
            try {
                val intent = Intent(this@ExpenseActivity, TransactionActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }



        loadData()
    }

    private fun loadData() {
        val userId = firebaseAuth.uid ?: return

        modelsActivityArrayList.clear()
        sumExpense = 0


        firebaseStore.collection("Expenses").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sortedList = task.result?.documents?.mapNotNull { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: return@mapNotNull null
                        sumExpense += amount
                        ModelsActivity(
                            document.id,
                            document.getString("note") ?: "",
                            amount.toString(),
                            "Expense",
                            document.getString("date") ?: return@mapNotNull null
                        )
                    }?.sortedByDescending {
                        it.date?.toDate() ?: Date(0)
                    } ?: listOf()

                    modelsActivityArrayList.addAll(sortedList)
                    runOnUiThread {
                        binding.sumExpenseExpense.text = sumExpense.toString()
                        binding.recyclerDashExpense.adapter =
                            AdapterActivity(this@ExpenseActivity, modelsActivityArrayList)
                    }
                }
            }

    }

}

