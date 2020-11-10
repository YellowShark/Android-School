package ru.yellowshark.surfandroidschool.data.repository

import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.db.entity.MemeEntity
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.Result
import ru.yellowshark.surfandroidschool.domain.User

class Repository(
    private val memesApi: MemesApi,
    private val memesDao: MemesDao,
    private val sessionManager: SessionManager
) {
    suspend fun login (login: String, password: String): Result {
        val response = memesApi.userAuth(
            AuthRequest(login, password)
        )
        return if (response.isSuccessful) {
            response.body()?.let {
                sessionManager.saveUser(
                    token = it.accessToken,
                    user = it.userInfo.toUser()
                )
            }
            Result.Success
        } else {
            Result.Error
        }
    }

    fun getLastSessionUserInfo(): User? = sessionManager.fetchUserInfo()

    fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

    suspend fun fetchPopularMemes(): List<Meme>? {
        val response = memesApi.getPopularMemes()

        return if (response.isSuccessful) {
            response.body()?.toMemeList()
        } else
            null
    }

    suspend fun saveMeme(memeEntity: MemeEntity) {
        memesDao.addCreatedMeme(memeEntity)
    }

    suspend fun getLocalMemes(): List<Meme>? = memesDao.getLocalMemes()
}