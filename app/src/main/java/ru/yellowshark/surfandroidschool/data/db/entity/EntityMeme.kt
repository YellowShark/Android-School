package ru.yellowshark.surfandroidschool.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_memes")
data class EntityMeme (
    @field:PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @ColumnInfo(name = "createdDate")
    val createdDate: Int,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean
)