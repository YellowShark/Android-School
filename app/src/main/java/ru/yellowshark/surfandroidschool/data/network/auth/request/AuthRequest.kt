package ru.yellowshark.surfandroidschool.data.network.auth.request

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("login")
    var login: String? = null,
    @SerializedName("password")
    val password: String? = null
)