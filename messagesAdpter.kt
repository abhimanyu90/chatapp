package com.av.avmessenger

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class messagesAdpter(var context: Context, var messagesAdpterArrayList: ArrayList<msgModelclass>) :
    RecyclerView.Adapter<Any?>() {
    var ITEM_SEND: Int = 1
    var ITEM_RECIVE: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false)
            return senderVierwHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false)
            return reciverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: Any, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages = messagesAdpterArrayList[position]
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).setTitle("Delete")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes") { dialogInterface, i -> }
                .setNegativeButton("No") { dialogInterface, i -> dialogInterface.dismiss() }.show()
            false
        }
        if (holder.javaClass == senderVierwHolder::class.java) {
            val viewHolder = holder as senderVierwHolder
            viewHolder.msgtxt.text = messages.message
            Picasso.get().load(chatwindo.senderImg).into(viewHolder.circleImageView)
        } else {
            val viewHolder = holder as reciverViewHolder
            viewHolder.msgtxt.text = messages.message
            Picasso.get().load(chatwindo.reciverIImg).into(viewHolder.circleImageView)
        }
    }

    override fun getItemCount(): Int {
        return messagesAdpterArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messagesAdpterArrayList[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == messages.senderid) {
            ITEM_SEND
        } else {
            ITEM_RECIVE
        }
    }

    internal inner class senderVierwHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circleImageView: CircleImageView = itemView.findViewById(R.id.profilerggg)
        var msgtxt: TextView = itemView.findViewById(R.id.msgsendertyp)
    }

    internal inner class reciverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circleImageView: CircleImageView = itemView.findViewById(R.id.pro)
        var msgtxt: TextView = itemView.findViewById(R.id.recivertextset)
    }
}
