package ru.yellowshark.surfandroidschool.data.network.auth.response


import com.google.gson.annotations.SerializedName
import ru.yellowshark.surfandroidschool.domain.User

data class UserInfo(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("userDescription")
    val userDescription: String,
    @SerializedName("username")
    val username: String
) {
    fun toUser() = User(username, firstName, lastName, userDescription)
}