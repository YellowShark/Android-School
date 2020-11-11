package ru.yellowshark.surfandroidschool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yellowshark.surfandroidschool.data.db.entity.EntityMeme
import ru.yellowshark.surfandroidschool.domain.Meme

@Dao
interface MemesDao {
    @Query("SELECT title, description, photoUrl, createdDate, isFavorite FROM table_memes")
    suspend fun getLocalMemes(): List<Meme>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCreatedMeme(entityMeme: EntityMeme)

    @Query("DELETE FROM table_memes")
    suspend fun clearAll()
}