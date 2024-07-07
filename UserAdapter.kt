package com.av.chattingapp

import android.R
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdpter<MainActivity>(mainActivity: MainActivity, var usersArrayList: ArrayList<Users>) :
    RecyclerView.Adapter<UserAdpter<Any?>.viewholder>() {
    var mainActivity: Context = mainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view: View =
            LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false)
        return viewholder(view)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val users = usersArrayList[position]
        holder.username.text = users.userName
        holder.userstatus.text = users.status
        Picasso.get().load(users.profilepic).into(holder.userimg)

        holder.itemView.setOnClickListener {
            val intent = Intent(mainActivity, chatwindo::class.java)
            intent.putExtra("nameeee", users.userName)
            intent.putExtra("reciverImg", users.profilepic)
            intent.putExtra("uid", users.userId)
            mainActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return usersArrayList.size
    }

    inner class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userimg: CircleImageView = itemView.findViewById(R.id.userimg)
        var username: TextView = itemView.findViewById<TextView>(R.id.username)
        var userstatus: TextView = itemView.findViewById<TextView>(R.id.userstatus)
    }
}