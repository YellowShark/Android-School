package ru.yellowshark.surfandroidschool.data.repository

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.network.AuthApi
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.model.User

class RepositoryImpl(
    private val memesApi: MemesApi,
    private val authApi: AuthApi,
    private val memesDao: MemesDao,
    private val sessionManager: SessionManager
) : Repository {
    override fun login(login: String, password: String): Single<Unit> {
        return authApi.userAuth(AuthRequest(login, password)).map {
            sessionManager.saveUser(it.accessToken, it.userInfo.fromResponseUserInfoToDomainUser())
        }
    }

    override fun logout(): Single<Unit> {
        return authApi.userLogout()
    }

    override fun getLastSessionUserInfo(): User = sessionManager.fetchUserInfo() ?: User.EMPTY

    override fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

    override fun forgetUser() = sessionManager.forgetUser()

    override fun getPopularMemes(): Single<List<Meme>> {
        return memesApi.getPopularMemes().map { it.fromResponseMemeListToDomainMemeList() }
    }

    override fun saveMeme(meme: Meme): Single<Unit> = memesDao.addCreatedMeme(meme.toDbLocalMeme())

    override fun getLocalMemes(): Single<List<Meme>?> = memesDao.getLocalMemes()

    override fun updateLocalMeme(meme: Meme) =
        memesDao.updateMemeByDate(meme.isFavorite, meme.createdDate)

    override fun cacheMemes(memes: List<Meme>): Single<Unit> {
        return memesDao.cacheMemes(memes.map { it.toDbCachedMeme() })
    }

    override fun getCachedMemesByTitle(query: String) =
        memesDao.getCachedMemesByTitle("%$query%")
}