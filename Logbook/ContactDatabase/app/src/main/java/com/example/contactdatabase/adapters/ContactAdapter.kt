package com.example.contactdatabase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactdatabase.R
import com.example.contactdatabase.models.Contact

class ContactAdapter(private val contactList: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ánh xạ các TextView VÀ ImageView
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDoB: TextView = itemView.findViewById(R.id.tvDoB)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = contactList[position]


        holder.tvName.text = currentContact.name
        holder.tvDoB.text = currentContact.dob
        holder.tvEmail.text = currentContact.email


        if (currentContact.avatarId != 0) {
            holder.ivAvatar.setImageResource(currentContact.avatarId)
        } else {

            holder.ivAvatar.setImageResource(R.drawable.avatar2)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}