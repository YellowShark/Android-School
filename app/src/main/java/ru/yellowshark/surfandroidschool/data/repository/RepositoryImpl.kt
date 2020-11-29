package ru.yellowshark.surfandroidschool.data.repository

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.data.db.MemesDao
import ru.yellowshark.surfandroidschool.data.network.AuthApi
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.model.User

class RepositoryImpl(
    private val memesApi: MemesApi,
    private val authApi: AuthApi,
    private val memesDao: MemesDao,
    private val sessionManager: SessionManager
) : Repository {

    override fun login(login: String, password: String): Single<AuthResponse> =
        authApi.userAuth(AuthRequest(login, password))

    override fun logout(): Single<Unit> = authApi.userLogout()

    override fun getLastSessionUserInfo(): User = sessionManager.fetchUserInfo() ?: User.EMPTY

    override fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

    override fun saveUser(token: String, user: User) = sessionManager.saveUser(token, user)

    override fun forgetUser() = sessionManager.forgetUser()

    override fun getPopularMemes(): Single<List<Meme>> =
        memesApi.getPopularMemes().map { it.toDomainMemeList() }

    override fun saveMeme(meme: Meme): Single<Unit> = memesDao.addCreatedMeme(meme.toDbLocalMeme())

    override fun getLocalMemes(): Single<List<Meme>?> = memesDao.getLocalMemes()

    override fun updateLocalMeme(meme: Meme) =
        memesDao.updateMemeByDate(meme.isFavorite, meme.createdDate)

    override fun cacheMemes(memes: List<Meme>): Single<Unit> =
        memesDao.cacheMemes(memes.map { it.toDbCachedMeme() })


    override fun getCachedMemesByTitle(query: String) =
        memesDao.getCachedMemesByTitle("%$query%")
}