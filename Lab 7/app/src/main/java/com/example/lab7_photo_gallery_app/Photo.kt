package com.example.lab7_photo_gallery_app

data class Photo(
    val id: Long,
    val resourceId: Int,
    val title: String,
    val category: String,
    var isSelected: Boolean = false
)
