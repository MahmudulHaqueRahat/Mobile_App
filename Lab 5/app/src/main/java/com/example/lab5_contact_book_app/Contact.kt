package com.example.lab5_contact_book_app

data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val initial: String = name.take(1).uppercase()
)
