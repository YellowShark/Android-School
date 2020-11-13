package ru.yellowshark.surfandroidschool.domain

import ru.yellowshark.surfandroidschool.data.db.entity.EntityCachedMeme

data class Meme(
    val title: String,
    val description: String,
    val photoUrl: String,
    val createdDate: Int,
    var isFavorite: Boolean,
) {
    fun toDbEntityCachedMeme() = EntityCachedMeme(null, title, description, photoUrl, createdDate, isFavorite)
}