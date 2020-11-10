package ru.yellowshark.surfandroidschool.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.yellowshark.surfandroidschool.data.db.entity.MemeEntity

@Database(entities = [MemeEntity::class], version = 3)
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
                context.applicationContext, MemesDatabase::class.java, "memes.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}