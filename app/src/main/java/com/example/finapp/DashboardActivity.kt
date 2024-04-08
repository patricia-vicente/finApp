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
    private var modelsActivityArrayList = ArrayList<ModelsActivity>()
    private lateinit var adapterActivity: AdapterActivity
    private var sumExpense: Int = 0
    private var sumIncome: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()//
        adapterActivity = AdapterActivity(this, modelsActivityArrayList)

        binding.recyclerDash2.layoutManager = LinearLayoutManager(this)
        binding.recyclerDash2.adapter = AdapterActivity(this, modelsActivityArrayList)
        binding.recyclerDash2.setHasFixedSize(true)

        binding.incBtnNav.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, IncomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.expBtnNav.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, ExpenseActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.transactionBar.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, TransactionActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.viewDashMain.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.viewIncMain.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, IncomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.viewExpMain.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, ExpenseActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {

            }
        }

        binding.viewTransMain.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, TransactionActivity::class.java)
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
        sumIncome = 0

        firebaseStore.collection("Expenses").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
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
                        modelsActivityArrayList.add(transaction)
                    }

                    runOnUiThread {
                        binding.sumExpense.text = sumExpense.toString()
                        binding.sumBalance.text = (sumIncome - sumExpense).toString()
                        val adapterActivity =
                            AdapterActivity(this@DashboardActivity, modelsActivityArrayList)
                        binding.recyclerDash2.adapter = adapterActivity
                    }
                } else {


                }
            }



        firebaseStore.collection("Incomes").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
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
                        modelsActivityArrayList.add(transaction)
                    }
                    runOnUiThread {
                        binding.sumIncome.text = sumIncome.toString()
                        binding.sumBalance.text = (sumIncome - sumExpense).toString()
                        val adapterActivity =
                            AdapterActivity(this@DashboardActivity, modelsActivityArrayList)
                        binding.recyclerDash2.adapter = adapterActivity
                    }
                } else {

                }
            }
    }

}


