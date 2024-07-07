package com.example.chattingapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.av.avmessenger.Users
import com.av.chattingapp.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class setting : AppCompatActivity() {
    var setprofile: ImageView? = null
    var setname: EditText? = null
    var setstatus: EditText? = null
    var donebut: Button? = null
    var auth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var setImageUri: Uri? = null
    var email: String? = null
    var password: String? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        setprofile = findViewById<ImageView>(R.id.settingprofile)
        setname = findViewById<EditText>(R.id.settingname)
        setstatus = findViewById<EditText>(R.id.settingstatus)
        donebut = findViewById<Button>(R.id.donebutt)

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Saing...")
        progressDialog!!.setCancelable(false)

        val reference = database!!.reference.child("user").child(auth!!.uid!!)
        val storageReference = storage!!.reference.child("upload").child(
            auth!!.uid!!
        )
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                email = snapshot.child("mail").value.toString()
                password = snapshot.child("password").value.toString()
                val name = snapshot.child("userName").value.toString()
                val profile = snapshot.child("profilepic").value.toString()
                val status = snapshot.child("status").value.toString()
                setname.setText(name)
                setstatus.setText(status)
                Picasso.get().load(profile).into(setprofile)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        setprofile.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10)
        })

        donebut.setOnClickListener(View.OnClickListener {
            progressDialog!!.show()
            val name = setname.getText().toString()
            val Status = setstatus.getText().toString()
            if (setImageUri != null) {
                storageReference.putFile(setImageUri!!).addOnCompleteListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val finalImageUri = uri.toString()
                        val users =
                            Users(auth!!.uid, name, email, password, finalImageUri, Status)
                        reference.setValue(users).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressDialog!!.dismiss()
                                Toast.makeText(
                                    this@setting,
                                    "Data Is save ",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(
                                        this@setting,
                                        MainActivity::class.java
                                    )
                                startActivity(intent)
                                finish()
                            } else {
                                progressDialog!!.dismiss()
                                Toast.makeText(
                                    this@setting,
                                    "Some thing went romg",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val finalImageUri = uri.toString()
                    val users = Users(auth!!.uid, name, email, password, finalImageUri, Status)
                    reference.setValue(users).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog!!.dismiss()
                            Toast.makeText(
                                this@setting,
                                "Data Is save ",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(
                                this@setting,
                                MainActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        } else {
                            progressDialog!!.dismiss()
                            Toast.makeText(
                                this@setting,
                                "Some thing went romg",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            if (data != null) {
                setImageUri = data.data
                setprofile!!.setImageURI(setImageUri)
            }
        }
    }
}