package com.av.avmessenger

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    var logsignup: TextView? = null
    var button: Button? = null
    var email: EditText? = null
    var password: EditText? = null
    var auth: FirebaseAuth? = null
    var emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please Wait...")
        progressDialog!!.setCancelable(false)
        supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        button = findViewById(R.id.logbutton)
        email = findViewById(R.id.editTexLogEmail)
        password = findViewById(R.id.editTextLogPassword)
        logsignup = findViewById(R.id.logsignup)

        logsignup!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@login, registration::class.java)
            startActivity(intent)
            finish()
        })


        button!!.setOnClickListener(View.OnClickListener {
            val Email = email!!.getText().toString()
            val pass = password!!.getText().toString()
            if ((TextUtils.isEmpty(Email))) {
                progressDialog!!.dismiss()
                Toast.makeText(this@login, "Enter The Email", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(pass)) {
                progressDialog!!.dismiss()
                Toast.makeText(this@login, "Enter The Password", Toast.LENGTH_SHORT).show()
            } else if (!Email.matches(emailPattern.toRegex())) {
                progressDialog!!.dismiss()
                email!!.setError("Give Proper Email Address")
            } else if (password!!.length() < 6) {
                progressDialog!!.dismiss()
                password!!.setError("More Then Six Characters")
                Toast.makeText(
                    this@login,
                    "Password Needs To Be Longer Then Six Characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth!!.signInWithEmailAndPassword(Email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog!!.show()
                        try {
                            val intent = Intent(this@login, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            Toast.makeText(this@login, e.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@login, task.exception!!.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }
}