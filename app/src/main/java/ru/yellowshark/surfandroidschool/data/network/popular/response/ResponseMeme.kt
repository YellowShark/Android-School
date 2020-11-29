package ru.yellowshark.surfandroidschool.data.network.popular.response

import com.google.gson.annotations.SerializedName
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

data class ResponseMeme (
    @SerializedName("createdDate")
    val createdDate: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("isFavorite")
    var isFavorite: Boolean,

    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("title")
    val title: String
) {
    fun toDomainMeme() = Meme(title, description, photoUrl, createdDate, isFavorite)
}