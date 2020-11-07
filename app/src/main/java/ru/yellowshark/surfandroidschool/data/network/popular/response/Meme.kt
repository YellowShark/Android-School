package ru.yellowshark.surfandroidschool.data.network.popular.response


import com.google.gson.annotations.SerializedName

data class Meme (
    @SerializedName("createdDate")
    val createdDate: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isFavorite")
    var isFavorite: Boolean,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("title")
    val title: String
)