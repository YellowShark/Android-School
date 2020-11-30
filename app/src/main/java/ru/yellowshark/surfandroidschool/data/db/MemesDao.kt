package ru.yellowshark.surfandroidschool.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.yellowshark.surfandroidschool.data.db.entity.EntityCachedMeme
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

private const val TABLE_LOCAL_MEMES = "table_local_memes"
private const val TABLE_CACHED_MEMES = "table_cached_memes"
private const val ALL_BUT_ID = "title, description, photoUrl, createdDate, isFavorite"

@Dao
interface MemesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCreatedMeme(entityMeme: EntityLocalMeme): Single<Unit>

    @Query("SELECT $ALL_BUT_ID FROM $TABLE_LOCAL_MEMES")
    fun getLocalMemes(): Single<List<Meme>?>

    @Query("UPDATE $TABLE_LOCAL_MEMES SET isFavorite = :isLiked WHERE createdDate = :createdDate")
    fun updateMemeByDate(isLiked: Boolean, createdDate: Int): Single<Unit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun cacheMemes(memes: List<EntityCachedMeme>): Single<Unit>

    @Query("SELECT $ALL_BUT_ID FROM $TABLE_CACHED_MEMES WHERE title LIKE :title")
    fun getCachedMemesByTitle(title: String): Single<List<Meme>?>

    @Query("SELECT $ALL_BUT_ID FROM $TABLE_CACHED_MEMES")
    fun getCachedMemes(): Single<List<Meme>?>
}