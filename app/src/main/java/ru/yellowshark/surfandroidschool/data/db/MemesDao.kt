package ru.yellowshark.surfandroidschool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.yellowshark.surfandroidschool.data.db.entity.Meme

@Dao
interface MemesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMemes(memes: List<Meme>)

    @Query("SELECT * FROM table_memes")
    suspend fun getCachedMemes(): List<Meme>
}