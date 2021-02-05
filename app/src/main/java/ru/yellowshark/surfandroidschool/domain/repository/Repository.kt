package ru.yellowshark.surfandroidschool.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.user.model.User

interface Repository {
    fun login(login: String, password: String): Single<AuthResponse>

    fun logout(): Completable

    fun getLastSessionUserInfo(): User

    fun getLastSessionToken(): String?

    fun saveUser(token: String, user: User)

    fun forgetUser()

    fun getPopularMemes(): Single<List<Meme>>

    fun saveMeme(meme: Meme): Completable

    fun getLocalMemes(): Single<List<Meme>?>

    fun updateLocalMeme(meme: Meme): Completable

    fun cacheMemes(memes: List<Meme>): Completable

    fun getCachedMemesByTitle(query: String): Single<List<Meme>?>
}