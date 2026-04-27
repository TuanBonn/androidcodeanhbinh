package com.example.contactdatabase.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.contactdatabase.R
import com.example.contactdatabase.models.Contact

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ContactsDB"

        private const val DATABASE_VERSION = 2

        private const val TABLE_DETAILS = "details"
        private const val COLUMN_ID = "person_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DOB = "dob"
        private const val COLUMN_EMAIL = "email"

        private const val COLUMN_AVATAR_ID = "avatar_id"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_TABLE_DETAILS = (
                "CREATE TABLE $TABLE_DETAILS (" +
                        "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$COLUMN_NAME TEXT," +
                        "$COLUMN_DOB TEXT," +
                        "$COLUMN_EMAIL TEXT," +
                        "$COLUMN_AVATAR_ID INTEGER)"
                )
        db.execSQL(CREATE_TABLE_DETAILS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (oldVersion < 2) {

            db.execSQL("ALTER TABLE $TABLE_DETAILS ADD COLUMN $COLUMN_AVATAR_ID INTEGER DEFAULT ${R.drawable.avatar1}")
        } else {

            db.execSQL("DROP TABLE IF EXISTS $TABLE_DETAILS")
            onCreate(db)
        }
    }


    fun addContact(name: String, dob: String, email: String, avatarId: Int): Long {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DOB, dob)
            put(COLUMN_EMAIL, email)
            put(COLUMN_AVATAR_ID, avatarId)
        }

        val id = db.insert(TABLE_DETAILS, null, values)
        db.close()
        return id
    }


    fun getAllContacts(): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()
        val selectQuery = "SELECT * FROM $TABLE_DETAILS ORDER BY $COLUMN_NAME ASC"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(COLUMN_ID)
                    val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
                    val dobIndex = cursor.getColumnIndex(COLUMN_DOB)
                    val emailIndex = cursor.getColumnIndex(COLUMN_EMAIL)
                    val avatarIndex = cursor.getColumnIndex(COLUMN_AVATAR_ID)

                    if (idIndex != -1 && nameIndex != -1 && dobIndex != -1 && emailIndex != -1 && avatarIndex != -1) {
                        val contact = Contact(
                            id = cursor.getLong(idIndex),
                            name = cursor.getString(nameIndex),
                            dob = cursor.getString(dobIndex),
                            email = cursor.getString(emailIndex),
                            avatarId = cursor.getInt(avatarIndex)
                        )
                        contactList.add(contact)
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return contactList
    }
}