package ru.yellowshark.surfandroidschool.data.network.auth.response


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("userInfo")
    val userInfo: UserInfo
)