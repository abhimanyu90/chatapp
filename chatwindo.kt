package com.av.avmessenger

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.Date

class chatwindo : AppCompatActivity() {
    var reciverimg: String? = null
    var reciverUid: String? = null
    var reciverName: String? = null
    var SenderUID: String? = null
    var profile: CircleImageView? = null
    var reciverNName: TextView? = null
    var database: FirebaseDatabase? = null
    var firebaseAuth: FirebaseAuth? = null
    var sendbtn: CardView? = null
    var textmsg: EditText? = null

    var senderRoom: String? = null
    var reciverRoom: String? = null
    var messageAdpter: RecyclerView? = null
    var messagesArrayList: ArrayList<msgModelclass>? = null
    var mmessagesAdpter: messagesAdpter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatwindo)
        supportActionBar!!.hide()
        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        reciverName = intent.getStringExtra("nameeee")
        reciverimg = intent.getStringExtra("reciverImg")
        reciverUid = intent.getStringExtra("uid")

        messagesArrayList = ArrayList()

        sendbtn = findViewById(R.id.sendbtnn)
        textmsg = findViewById(R.id.textmsg)
        reciverNName = findViewById(R.id.recivername)
        profile = findViewById(R.id.profileimgg)
        messageAdpter = findViewById(R.id.msgadpter)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.setStackFromEnd(true)
        messageAdpter!!.setLayoutManager(linearLayoutManager)
        mmessagesAdpter = messagesAdpter(this@chatwindo, messagesArrayList!!)
        messageAdpter!!.setAdapter(mmessagesAdpter)


        Picasso.get().load(reciverimg).into(profile)
        reciverNName!!.setText("" + reciverName)

        SenderUID = firebaseAuth!!.getUid()

        senderRoom = SenderUID + reciverUid
        reciverRoom = reciverUid + SenderUID


        val reference: DatabaseReference? =
            firebaseAuth!!.getUid()?.let { database!!.getReference().child("user").child(it) }
        val chatreference: DatabaseReference =
            database!!.getReference().child("chats").child(senderRoom!!).child("messages")


        chatreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot:
                                      DataSnapshot) {
                messagesArrayList!!.clear()
                for (dataSnapshot in snapshot.getChildren()) {
                    val messages: msgModelclass? = dataSnapshot.getValue<msgModelclass>(
                        msgModelclass::class.java
                    )
                    if (messages != null) {
                        messagesArrayList!!.add(messages)
                    }
                }
                mmessagesAdpter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString()
                reciverIImg = reciverimg
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        sendbtn!!.setOnClickListener(View.OnClickListener {
            val message: String = textmsg!!.getText().toString()
            if (message.isEmpty()) {
                Toast.makeText(this@chatwindo, "Enter The Message First", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            textmsg!!.setText("")
            val date = Date()
            val messagess = msgModelclass(message, SenderUID, date.time)

            database = FirebaseDatabase.getInstance()
            database!!.getReference().child("chats")
                .child(senderRoom!!)
                .child("messages")
                .push().setValue(messagess)
                .addOnCompleteListener(object : OnCompleteListener<Void?> {
                    override fun onComplete(p0: Task<Void?>) {
                        database!!.getReference().child("chats")
                            .child(reciverRoom!!)
                            .child("messages")
                            .push().setValue(messagess)
                            .addOnCompleteListener(object : OnCompleteListener<Void?> {

                                override fun onComplete(p0: Task<Void?>) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
                })
        })
    }

    companion object {
        @JvmField
        var senderImg: String? = null
        @JvmField
        var reciverIImg: String? = null
    }
}