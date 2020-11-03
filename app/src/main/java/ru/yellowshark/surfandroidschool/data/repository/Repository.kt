package ru.yellowshark.surfandroidschool.data.repository

import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.network.popular.response.Meme

class Repository(
    private val memesApi: MemesApi
) {
    suspend fun login (authRequest: AuthRequest): AuthResponse? {
        val response = memesApi.userAuth(authRequest)

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun fetchPopularMemes(): List<Meme>? {
        val response = memesApi.getPopularMemes()

        return if (response.isSuccessful)
            response.body()
        else
            null
    }
}