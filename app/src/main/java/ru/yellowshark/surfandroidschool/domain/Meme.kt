package ru.yellowshark.surfandroidschool.domain

data class Meme(
    val title: String,
    val description: String,
    val photoUrl: String,
    val createdDate: Int,
    var isFavorite: Boolean,
)