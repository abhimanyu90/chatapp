package com.av.avmessenger

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var mainUserRecyclerView: RecyclerView? = null
    var adapter: UserAdpter? = null
    var database: FirebaseDatabase? = null
    var usersArrayList: ArrayList<Users?>? = null
    var imglogout: ImageView? = null
    var cumbut: ImageView? = null
    var setbut: ImageView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        cumbut = findViewById(R.id.camBut)
        setbut = findViewById(R.id.settingBut)

        val reference = database!!.reference.child("user")

        usersArrayList = ArrayList()

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView)
        mainUserRecyclerView!!.setLayoutManager(LinearLayoutManager(this))
        adapter = UserAdpter(this@MainActivity, usersArrayList)
        mainUserRecyclerView!!.setAdapter(adapter)


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val users = dataSnapshot.getValue(Users::class.java)
                    usersArrayList!!.add(users)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        imglogout = findViewById(R.id.logoutimg)

        imglogout!!.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(this@MainActivity, R.style.dialoge)
            dialog.setContentView(R.layout.dialog_layout)
            val yes = dialog.findViewById<Button>(R.id.yesbnt)
            val no = dialog.findViewById<Button>(R.id.nobnt)
            yes.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, login::class.java)
                startActivity(intent)
                finish()
            }
            no.setOnClickListener { dialog.dismiss() }
            dialog.show()
        })

        setbut!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, setting::class.java)
            startActivity(intent)
        })

        cumbut!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 10)
        })

        if (auth!!.currentUser == null) {
            val intent = Intent(this@MainActivity, login::class.java)
            startActivity(intent)
        }
    }
}