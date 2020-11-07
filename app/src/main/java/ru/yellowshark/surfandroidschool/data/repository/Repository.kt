package ru.yellowshark.surfandroidschool.data.repository

import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.db.entity.Meme
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.network.auth.response.UserInfo

class Repository(
    private val memesApi: MemesApi,
    private val memesDao: MemesDao,
    private val sessionManager: SessionManager
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

    fun getLastSessionUserInfo(): UserInfo? = sessionManager.fetchUserInfo()
}