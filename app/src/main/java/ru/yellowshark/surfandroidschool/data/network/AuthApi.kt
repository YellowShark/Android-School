package ru.yellowshark.surfandroidschool.data.network

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse

interface AuthApi {
    @POST("auth/login")
    fun userAuth(@Body request: AuthRequest): Single<AuthResponse>
    @POST("auth/logout")
    fun userLogout(): Completable
}