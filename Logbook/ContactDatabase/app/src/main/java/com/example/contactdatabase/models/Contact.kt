package com.example.contactdatabase.models

data class Contact(
    val id: Long,
    val name: String,
    val dob: String,
    val email: String,
    val avatarId: Int
)