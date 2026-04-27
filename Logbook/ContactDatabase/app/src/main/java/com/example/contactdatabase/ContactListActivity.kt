package com.example.contactdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactdatabase.adapters.ContactAdapter
import com.example.contactdatabase.helpers.DatabaseHelper
import com.example.contactdatabase.models.Contact


class ContactListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private var contactList: ArrayList<Contact> = ArrayList()
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)


        recyclerView = findViewById(R.id.rvContacts)


        dbHelper = DatabaseHelper(this)
        contactAdapter = ContactAdapter(contactList)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter


        loadContacts()
    }


    private fun loadContacts() {
        contactList.clear()
        contactList.addAll(dbHelper.getAllContacts())
        contactAdapter.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
        loadContacts()
    }
}