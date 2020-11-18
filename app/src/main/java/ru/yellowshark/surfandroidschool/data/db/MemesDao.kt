package ru.yellowshark.surfandroidschool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yellowshark.surfandroidschool.data.db.entity.EntityCachedMeme
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.domain.Meme

const val TABLE_LOCAL_MEMES = "table_local_memes"
const val TABLE_CACHED_MEMES = "table_cached_memes"

@Dao
interface MemesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCreatedMeme(entityMeme: EntityLocalMeme)

    @Query("SELECT title, description, photoUrl, createdDate, isFavorite FROM $TABLE_LOCAL_MEMES")
    suspend fun getLocalMemes(): List<Meme>?

    @Query("UPDATE $TABLE_LOCAL_MEMES SET isFavorite = :isLiked WHERE createdDate = :createdDate")
    suspend fun updateMemeByDate(isLiked: Boolean, createdDate: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMemes(memes: List<EntityCachedMeme>)

    @Query("SELECT title, description, photoUrl, createdDate, isFavorite FROM $TABLE_CACHED_MEMES WHERE title LIKE :title")
    suspend fun getCachedMemesByTitle(title: String): List<Meme>?

    @Query("SELECT title, description, photoUrl, createdDate, isFavorite FROM $TABLE_CACHED_MEMES")
    suspend fun getCachedMemes(): List<Meme>?
}