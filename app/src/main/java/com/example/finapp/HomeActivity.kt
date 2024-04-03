package com.example.finapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var buttonBar: ImageButton
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout = findViewById(R.id.drawer_layout)

        buttonBar = findViewById(R.id.buttonBar)
        val navigationView: NavigationView = findViewById(R.id.naView)

        bottomNavBar = findViewById(R.id.bottomNavBar)

        frameLayout = findViewById(R.id.frameLayout)

        buttonBar.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    Toast.makeText(this@HomeActivity, "Dashboard Clicked", Toast.LENGTH_SHORT)
                        .show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.income -> {
                    Toast.makeText(this@HomeActivity, "Dashboard Clicked", Toast.LENGTH_SHORT)
                        .show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.expense -> {
                    Toast.makeText(this@HomeActivity, "Dashboard Clicked", Toast.LENGTH_SHORT)
                        .show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }

        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> loadFragment(DashboardFragment(), false)
                R.id.income -> loadFragment(IncomeFragment(), false)
                else -> loadFragment(ExpensesFragment(), false)
            }
            true
        }

        loadFragment(DashboardFragment(), true)
    }



        private fun loadFragment(fragment: Fragment, isAppInitialized: Boolean) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            if (isAppInitialized) {
                fragmentTransaction.add(R.id.frameLayout, fragment)
            } else {
                fragmentTransaction.replace(R.id.frameLayout, fragment)
            }

            fragmentTransaction.commit()
        }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Actual implementation here
        when (item.itemId) {
            R.id.dashboard -> Toast.makeText(this, "Dashboard Clicked", Toast.LENGTH_SHORT).show()
            R.id.income -> Toast.makeText(this, "Income Clicked", Toast.LENGTH_SHORT).show()
            R.id.expense -> Toast.makeText(this, "Expense Clicked", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}






