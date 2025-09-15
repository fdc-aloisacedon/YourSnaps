package com.example.yoursnaps.domain

data class GalleryDataClass(
    val photo: String, // or use Uri/String if images are from storage
    val title: String?
)