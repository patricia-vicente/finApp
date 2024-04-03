package com.example.finapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var eEmail: EditText
    private lateinit var ePass: EditText
    private lateinit var eLoginBtn: Button
    private lateinit var eForgetPass: TextView
    private lateinit var eSignUpNow: TextView
    private lateinit var progressDialog: AlertDialog

    private lateinit var eAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login()
        initFirebase()
    }

    private fun initFirebase() {
        eAuth=FirebaseAuth.getInstance()
    }

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
}