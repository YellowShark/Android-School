package ru.yellowshark.surfandroidschool.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.yellowshark.surfandroidschool.data.db.entity.EntityCachedMeme
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme

private const val DB_NAME = "memes.db"

@Database(entities = [EntityLocalMeme::class, EntityCachedMeme::class], version = 5)
abstract class MemesDatabase : RoomDatabase() {
    abstract fun memesDao(): MemesDao

    companion object {
        @Volatile
        private var INSTANCE: MemesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext, MemesDatabase::class.java, DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}