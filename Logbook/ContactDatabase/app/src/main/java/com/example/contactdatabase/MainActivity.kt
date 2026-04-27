package com.example.contactdatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.contactdatabase.helpers.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDoB: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var btnViewAll: Button
    private lateinit var rgAvatar: RadioGroup
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        etName = findViewById(R.id.etName)
        etDoB = findViewById(R.id.etDoB)
        etEmail = findViewById(R.id.etEmail)
        btnSave = findViewById(R.id.btnSave)
        btnViewAll = findViewById(R.id.btnViewAll)
        rgAvatar = findViewById(R.id.rgAvatar)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnSave.setOnClickListener {
            addNewContact()
        }

        btnViewAll.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addNewContact() {
        val name = etName.text.toString().trim()
        val dob = etDoB.text.toString().trim()
        val email = etEmail.text.toString().trim()

        // VALIDATION
        if (name.isEmpty() || dob.isEmpty() || email.isEmpty()) {
            // SỬA LỖI: Dùng string resource
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val selectedAvatarId = getSelectedAvatarId()
        val id = dbHelper.addContact(name, dob, email, selectedAvatarId)

        if (id != -1L) {
            val successMessage = getString(R.string.success_contact_added, id)
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
            clearInputs()
        } else {

            Toast.makeText(this, getString(R.string.error_add_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSelectedAvatarId(): Int {
        val selectedRadioButtonId = rgAvatar.checkedRadioButtonId

        return when (selectedRadioButtonId) {
            R.id.rbAvatar1 -> R.drawable.avatar1
            R.id.rbAvatar2 -> R.drawable.avatar2
            R.id.rbAvatar3 -> R.drawable.avatar3
            else -> R.drawable.avatar1
        }
    }

    private fun clearInputs() {
        etName.text.clear()
        etDoB.text.clear()
        etEmail.text.clear()
        rgAvatar.check(R.id.rbAvatar1)
    }
}