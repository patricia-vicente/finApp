package com.example.finapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.databinding.ActivityRegBinding
import com.google.firebase.auth.FirebaseAuth

class RegActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegBinding
    private lateinit var eAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eAuth = FirebaseAuth.getInstance()

        binding.signIn.setOnClickListener {
            val intent= Intent(this@RegActivity,MainActivity::class.java)
            try{
                startActivity(intent);
            } catch (e: Exception) {

            }
        }

        binding.regBtn.setOnClickListener {
            val email = binding.regEmail.text.toString()
            val password = binding.regPassword.text.toString()
            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                Toast.makeText(
                    this@RegActivity,
                    "Email and password cannot be empty.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            eAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    Toast.makeText(this@RegActivity, "User Registered", Toast.LENGTH_SHORT).show()
                }

                .addOnFailureListener { e ->
                    Toast.makeText(this@RegActivity, e.message, Toast.LENGTH_SHORT).show()
                }


        }
    }
}



/*

private lateinit var eEmail: EditText
    private lateinit var ePass: EditText
    private lateinit var eRegBtn: Button
    private lateinit var eSignIn: TextView
    private lateinit var progressDialog: AlertDialog
    private fun initFirebase() {
        eAuth=FirebaseAuth.getInstance()
    }
  /*reg()
        initFirebase()*/
    private fun reg() {

        eEmail=findViewById(R.id.reg_email)
        ePass=findViewById(R.id.reg_password)
        eRegBtn=findViewById(R.id.reg_btn)
        eSignIn=findViewById(R.id.sign_in)

        progressDialog = AlertDialog.Builder(this).apply {
            setView(LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout, null))
            setCancelable(false)
        }.create()

        eRegBtn.setOnClickListener {
            val email=eEmail.text.toString().trim()
            val pass=ePass.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                eEmail.error = "Email required"
            }
            if (TextUtils.isEmpty(pass)) {
                ePass.error = "Password Required"

            }

            progressDialog.setTitle("Please Wait")
            progressDialog.show()

            eAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                progressDialog.dismiss()

                if (task.isSuccessful) {
                    Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))

                } else {
                    val errorMessage = task.exception?.message ?: "Register failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

        }

        eSignIn.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}*/