package ru.yellowshark.surfandroidschool.data.repository

import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
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
    suspend fun login(login: String, password: String): Result<Nothing> {
        val response = memesApi.userAuth(
            AuthRequest(login, password)
        )
        return if (response.isSuccessful) {
            response.body()?.let {
                sessionManager.saveUser(
                    token = it.accessToken,
                    user = it.userInfo.fromResponseUserInfoToDomainUser()
                )
            }
            Result.Success()
        } else {
            Result.Error
        }
    }

    suspend fun logout(): Result<Nothing> {
        val response = memesApi.userLogout()
        return if (response.isSuccessful) {
            sessionManager.forgetUser()
            Result.Success()
        } else
            Result.Error
    }

    fun getLastSessionUserInfo(): User? = sessionManager.fetchUserInfo()

    fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

    suspend fun fetchPopularMemes(): Result<List<Meme>> {
        val response = memesApi.getPopularMemes()
        return if (response.isSuccessful) {
            Result.Success(response.body()?.fromResponseMemeListToDomainMemeList())
        } else
            Result.Error
    }

    suspend fun saveMeme(entityMeme: EntityLocalMeme) {
        memesDao.addCreatedMeme(entityMeme)
    }

    suspend fun getLocalMemes(): List<Meme>? = memesDao.getLocalMemes() ?: emptyList()

    suspend fun cacheMemes(memes: List<Meme>) =
        memesDao.cacheMemes(memes.map { it.toDbEntityCachedMeme() })

    suspend fun getCachedMemesByTitle(query: String) = memesDao.getCachedMemes("%$query%") ?: emptyList()
}