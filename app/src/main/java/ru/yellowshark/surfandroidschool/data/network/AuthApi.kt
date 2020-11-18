package ru.yellowshark.surfandroidschool.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse

interface AuthApi {
    @POST("auth/login")
    suspend fun userAuth(@Body request: AuthRequest): Response<AuthResponse>
    @POST("auth/logout")
    suspend fun userLogout(): Response<*>
}