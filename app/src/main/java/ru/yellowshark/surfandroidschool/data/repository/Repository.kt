package ru.yellowshark.surfandroidschool.data.repository

import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.Result
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.internal.NoConnectivityException
import ru.yellowshark.surfandroidschool.internal.NothingFoundException

class Repository(
    private val memesApi: MemesApi,
    private val memesDao: MemesDao,
    private val sessionManager: SessionManager
) {
    suspend fun login(login: String, password: String): Result<*> {
        return try {
            val response = memesApi.userAuth(
                AuthRequest(login, password)
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    sessionManager.saveUser(
                        token = it.accessToken,
                        user = it.userInfo.fromResponseUserInfoToDomainUser()
                    )
                }
                Result.Success<Nothing>()
            } else
                Result.Error()
        } catch (e: NoConnectivityException) {
            Result.Error(e)
        }

    }

    suspend fun logout(): Result<*> {
        return try {
            val response = memesApi.userLogout()
            if (response.isSuccessful) {
                sessionManager.forgetUser()
                Result.Success<Nothing>()
            } else
                Result.Error()
        } catch (e: NoConnectivityException) {
            Result.Error(e)
        }
    }

    fun getLastSessionUserInfo(): User? = sessionManager.fetchUserInfo()

    fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

    suspend fun fetchPopularMemes(): Result<*> {
        return try {
            val response = memesApi.getPopularMemes()
            if (response.isSuccessful) {
                Result.Success(response.body()?.fromResponseMemeListToDomainMemeList())
            } else
                Result.Error(NothingFoundException())
        } catch (e: NoConnectivityException) {
            Result.Error(e)
        }
    }

    suspend fun saveMeme(entityMeme: EntityLocalMeme) {
        memesDao.addCreatedMeme(entityMeme)
    }

    suspend fun getLocalMemes(): List<Meme>? = memesDao.getLocalMemes() ?: emptyList()

    suspend fun updateLocalMeme(meme: Meme) =
        memesDao.updateMemeByDate(meme.isFavorite, meme.createdDate)

    suspend fun cacheMemes(memes: List<Meme>) {
        val newMemes = ArrayList<Meme>(memes)
        val alreadyCachedMemes = memesDao.getCachedMemesByTitle("") ?: return
        memes.forEach {
            if (alreadyCachedMemes.contains(it))
                newMemes.remove(it)
        }
        if (newMemes.isNotEmpty())
            memesDao.cacheMemes(newMemes.map { it.toDbEntityCachedMeme() })
    }

    suspend fun getCachedMemesByTitle(query: String) =
        memesDao.getCachedMemesByTitle("%$query%") ?: emptyList()
}