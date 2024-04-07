package com.example.finapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityDashboardBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var modelsActivityArrayList = ArrayList<ModelsActivity>()
    private lateinit var adapterActivity: AdapterActivity
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var sumExpense: Int = 0
    private var sumIncome: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.naView)
        drawerLayout = findViewById(R.id.drawer_layout)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()//
        adapterActivity = AdapterActivity(this, modelsActivityArrayList)

        binding.recyclerDash.layoutManager = LinearLayoutManager(this)
        binding.recyclerDash.adapter = AdapterActivity(this, modelsActivityArrayList)
        binding.recyclerDash.setHasFixedSize(true)

        binding.btnDash.setOnClickListener {
            binding.drwer.openDrawer(GravityCompat.START)
        }

        binding.naView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                // IDs dos itens do menu
                R.id.dashboard -> {
                    Toast.makeText(this, "Dashboard Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.income -> {
                    Toast.makeText(this, "Income Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.expense -> {
                    Toast.makeText(this, "Expense Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }



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
        binding.btnRefresh.setOnClickListener {
            try {
                val intent = Intent(this@DashboardActivity, DashboardActivity::class.java)
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
                        binding.balance.text = (sumIncome - sumExpense).toString()
                        val adapterActivity =
                            AdapterActivity(this@DashboardActivity, modelsActivityArrayList)
                        binding.recyclerDash.adapter = adapterActivity
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
                        binding.balance.text = (sumIncome - sumExpense).toString()
                        val adapterActivity =
                            AdapterActivity(this@DashboardActivity, modelsActivityArrayList)
                        binding.recyclerDash.adapter = adapterActivity
                    }
                } else {

                }
            }
    }

}


