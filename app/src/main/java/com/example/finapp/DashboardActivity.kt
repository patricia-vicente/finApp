package com.example.finapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var bottomNavBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavBar)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.transaction -> {
                    val intent = Intent(this, TransactionActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.income -> {
                    // Inicie a Activity correspondente ao item "Dashboard"
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_notifications -> {
                    // Inicie a Activity correspondente ao item "Notifications"
                    val intent = Intent(this, NotificationsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}

    }
}