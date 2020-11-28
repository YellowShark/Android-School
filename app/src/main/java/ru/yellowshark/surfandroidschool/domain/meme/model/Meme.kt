package ru.yellowshark.surfandroidschool.domain.meme.model

import ru.yellowshark.surfandroidschool.data.db.entity.EntityCachedMeme
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme

data class Meme(
    val title: String,
    val description: String,
    val photoUrl: String,
    val createdDate: Int,
    var isFavorite: Boolean,
) {
    fun toDbCachedMeme() = EntityCachedMeme(null, title, description, photoUrl, createdDate, isFavorite)
    fun toDbLocalMeme() = EntityLocalMeme(null, title, description, photoUrl, createdDate, isFavorite)
}