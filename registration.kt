package com.av.avmessenger

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class registration : AppCompatActivity() {
    var loginbut: TextView? = null
    var rg_username: EditText? = null
    var rg_email: EditText? = null
    var rg_password: EditText? = null
    var rg_repassword: EditText? = null
    var rg_signup: Button? = null
    var rg_profileImg: CircleImageView? = null
    var auth: FirebaseAuth? = null
    var imageURI: Uri? = null
    var imageuri: String? = null
    var emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Establishing The Account")
        progressDialog!!.setCancelable(false)
        supportActionBar!!.hide()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        loginbut = findViewById(R.id.loginbut)
        rg_username = findViewById(R.id.rgusername)
        rg_email = findViewById(R.id.rgemail)
        rg_password = findViewById(R.id.rgpassword)
        rg_repassword = findViewById(R.id.rgrepassword)
        rg_profileImg = findViewById(R.id.profilerg0)
        rg_signup = findViewById(R.id.signupbutton)


        loginbut!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@registration, login::class.java)
            startActivity(intent)
            finish()
        })

        rg_signup!!.setOnClickListener(View.OnClickListener {
            val namee = rg_username!!.getText().toString()
            val emaill = rg_email!!.getText().toString()
            val Password = rg_password!!.getText().toString()
            val cPassword = rg_repassword!!.getText().toString()
            val status = "Hey I'm Using This Application"
            if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) ||
                TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword)
            ) {
                progressDialog!!.dismiss()
                Toast.makeText(
                    this@registration,
                    "Please Enter Valid Information",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!emaill.matches(emailPattern.toRegex())) {
                progressDialog!!.dismiss()
                rg_email!!.setError("Type A Valid Email Here")
            } else if (Password.length < 6) {
                progressDialog!!.dismiss()
                rg_password!!.setError("Password Must Be 6 Characters Or More")
            } else if (Password != cPassword) {
                progressDialog!!.dismiss()
                rg_password!!.setError("The Password Doesn't Match")
            } else {
                auth!!.createUserWithEmailAndPassword(emaill, Password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val id = task.result.user!!.uid
                            val reference = database!!.reference.child("user").child(id)
                            val storageReference = storage!!.reference.child("Upload").child(id)

                            if (imageURI != null) {
                                storageReference.putFile(imageURI!!).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                                            imageuri = uri.toString()
                                            val users =
                                                Users(id, namee, emaill, Password, imageuri, status)
                                            reference.setValue(users)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        progressDialog!!.show()
                                                        val intent = Intent(
                                                            this@registration,
                                                            MainActivity::class.java
                                                        )
                                                        startActivity(intent)
                                                        finish()
                                                    } else {
                                                        Toast.makeText(
                                                            this@registration,
                                                            "Error in creating the user",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }
                                    }
                                }
                            } else {
                                val status = "Hey I'm Using This Application"
                                imageuri =
                                    "https://firebasestorage.googleapis.com/v0/b/av-messenger-dc8f3.appspot.com/o/man.png?alt=media&token=880f431d-9344-45e7-afe4-c2cafe8a5257"
                                val users = Users(id, namee, emaill, Password, imageuri, status)
                                reference.setValue(users).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        progressDialog!!.show()
                                        val intent =
                                            Intent(this@registration, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@registration,
                                            "Error in creating the user",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@registration,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })


        rg_profileImg!!.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            if (data != null) {
                imageURI = data.data
                rg_profileImg!!.setImageURI(imageURI)
            }
        }
    }
}