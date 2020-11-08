package ru.yellowshark.surfandroidschool.data.db.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "table_memes")
data class Meme (
    @SerializedName("createdDate")
    @ColumnInfo(name = "createdDate")
    val createdDate: Int,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("isFavorite")
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean,

    @SerializedName("photoUrl")
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String
) {
    @PrimaryKey(autoGenerate = true)
    var columnId: Int = 0
}