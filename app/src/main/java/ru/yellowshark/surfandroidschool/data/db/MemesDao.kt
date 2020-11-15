package ru.yellowshark.surfandroidschool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yellowshark.surfandroidschool.data.db.entity.EntityCachedMeme
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.domain.Meme

@Dao
interface MemesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCreatedMeme(entityMeme: EntityLocalMeme)

    @Query("SELECT title, description, photoUrl, createdDate, isFavorite FROM table_local_memes")
    suspend fun getLocalMemes(): List<Meme>?

    @Query("UPDATE table_local_memes SET isFavorite = :isLiked WHERE createdDate = :createdDate")
    suspend fun updateMemeByDate(isLiked: Boolean, createdDate: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMemes(memes: List<EntityCachedMeme>)

    @Query("SELECT title, description, photoUrl, createdDate, isFavorite FROM table_cached_memes WHERE title LIKE :title")
    suspend fun getCachedMemes(title: String): List<Meme>?
}