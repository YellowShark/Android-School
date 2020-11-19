package ru.yellowshark.surfandroidschool.data.repository

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.data.network.AuthApi
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.User

class Repository(
    private val memesApi: MemesApi,
    private val authApi: AuthApi,
    private val memesDao: MemesDao,
    private val sessionManager: SessionManager
) {
    fun login(login: String, password: String): Single<Unit> {
        return authApi.userAuth(AuthRequest(login, password)).map {
            sessionManager.saveUser(it.accessToken, it.userInfo.fromResponseUserInfoToDomainUser())
        }
    }

    fun logout(): Single<Unit> {
        return authApi.userLogout()
    }

    fun getLastSessionUserInfo(): User? = sessionManager.fetchUserInfo()

    fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

    fun forgetUser() = sessionManager.forgetUser()

    fun fetchPopularMemes(): Single<List<Meme>> {
        return memesApi.getPopularMemes().map { it.fromResponseMemeListToDomainMemeList() }
    }

    suspend fun saveMeme(entityMeme: EntityLocalMeme) {
        memesDao.addCreatedMeme(entityMeme)
    }

    suspend fun getLocalMemes(): List<Meme>? = memesDao.getLocalMemes() ?: emptyList()

    suspend fun updateLocalMeme(meme: Meme) =
        memesDao.updateMemeByDate(meme.isFavorite, meme.createdDate)

    fun cacheMemes(memes: List<Meme>) {
        val newMemes = ArrayList<Meme>(memes)
        val alreadyCachedMemes = memesDao.getCachedMemes() ?: return
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