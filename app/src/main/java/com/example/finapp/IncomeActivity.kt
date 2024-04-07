package com.example.finapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finapp.databinding.ActivityIncomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class IncomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomeBinding
    private lateinit var firebaseStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var modelsActivityArrayList = ArrayList<ModelsActivity>()
    private lateinit var adapterActivity: AdapterActivity
    private var sumIncome: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseStore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        adapterActivity = AdapterActivity(this, modelsActivityArrayList)

        binding.recyclerDashIncome.layoutManager = LinearLayoutManager(this)
        binding.recyclerDashIncome.adapter =
            AdapterActivity(this, modelsActivityArrayList)
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

        modelsActivityArrayList.clear()
        sumIncome = 0

        firebaseStore.collection("Incomes").document(userId).collection("Notes")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val sortedList = task.result?.documents?.mapNotNull { document ->
                        val amount = document.getString("amount")?.toIntOrNull() ?: return@mapNotNull null
                        sumIncome += amount
                        ModelsActivity(
                            document.id,
                            document.getString("note") ?: "",
                            amount.toString(),
                            "Income",
                            document.getString("date") ?: return@mapNotNull null
                        )
                    }?.sortedByDescending {
                        it.date?.toDate() ?: Date(0)
                    } ?: listOf()

                    modelsActivityArrayList.addAll(sortedList)
                    runOnUiThread {
                        binding.sumIncomeIncome.text = sumIncome.toString()
                        binding.recyclerDashIncome.adapter =
                            AdapterActivity(this@IncomeActivity, modelsActivityArrayList)
                    }
                }
            }

    }

}

fun String.toDate(): Date? {
    return try {
        SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }
}
