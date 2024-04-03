package com.example.finapp

import android.app.AlertDialog
import android.app.Dialog
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

class RegActivity : AppCompatActivity() {

    private lateinit var eEmail: EditText
    private lateinit var ePass: EditText
    private lateinit var eRegBtn: Button
    private lateinit var eSignIn: TextView
    private lateinit var progressDialog: AlertDialog

    private lateinit var eAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
        reg()
        initFirebase()

    }

    private fun initFirebase() {
        eAuth=FirebaseAuth.getInstance()
    }

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
}