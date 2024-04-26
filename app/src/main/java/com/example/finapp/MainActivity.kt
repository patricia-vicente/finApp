package com.example.finapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var eAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eAuth = FirebaseAuth.getInstance()

        eAuth.addAuthStateListener(FirebaseAuth.AuthStateListener { eAuth ->
            val user = eAuth.currentUser
            if (user != null) {
                try {
                    val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {

                }
            }
        })


        binding.signUp.setOnClickListener {
            val intent = Intent(this@MainActivity, RegActivity::class.java)
            try {
                startActivity(intent)
            } catch (e: Exception) {

            }
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Email and password cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            eAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT).show()
                }
        }
    }


}






    /*
    private fun initFirebase() {
        eAuth=FirebaseAuth.getInstance()
    }


     */
    /*login()
        initFirebase()*/
         /* private lateinit var eEmail: EditText
    private lateinit var ePass: EditText
    private lateinit var eLoginBtn: Button
    private lateinit var eForgetPass: TextView
    private lateinit var eSignUpNow: TextView
    private lateinit var progressDialog: AlertDialog */


     /*

    private fun login() {
        eEmail = findViewById(R.id.login_email)
        ePass = findViewById(R.id.login_password)
        eLoginBtn = findViewById(R.id.login_btn)
        eForgetPass = findViewById(R.id.forget_password)
        eSignUpNow = findViewById(R.id.register_here)

        progressDialog = AlertDialog.Builder(this).apply {
            setView(LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout, null))
            setCancelable(false)
        }.create()

        eLoginBtn.setOnClickListener {
            val email = eEmail.text.toString().trim()
            val pass = ePass.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                eEmail.error = "Email required"
            }
            if (TextUtils.isEmpty(pass)) {
                ePass.error = "Password Required"
            }

            progressDialog.setTitle("Processing")
            progressDialog.show()

            eAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }



        }

        //Registration

        eSignUpNow.setOnClickListener {
            val intent = Intent(applicationContext, RegActivity::class.java)
            startActivity(intent)
        }

        //Reset Password

        eForgetPass.setOnClickListener{
            val intent = Intent(applicationContext, ResActivity::class.java)
            startActivity(intent)
        }


    }
}*/