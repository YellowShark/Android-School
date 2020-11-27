package ru.yellowshark.surfandroidschool.data.network.auth.response


import com.google.gson.annotations.SerializedName
import ru.yellowshark.surfandroidschool.domain.user.model.User

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
    fun fromResponseUserInfoToDomainUser() = User(username, firstName, lastName, userDescription)
}