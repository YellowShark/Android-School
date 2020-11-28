package ru.yellowshark.surfandroidschool.domain.repository

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.user.model.User

interface Repository {
    fun login(login: String, password: String): Single<Unit>

    fun logout(): Single<Unit>

    fun getLastSessionUserInfo(): User

    fun getLastSessionToken(): String?

    fun forgetUser()

    fun getPopularMemes(): Single<List<Meme>>

    fun saveMeme(meme: Meme): Single<Unit>

    fun getLocalMemes(): Single<List<Meme>?>

    fun updateLocalMeme(meme: Meme): Single<Unit>

    fun cacheMemes(memes: List<Meme>): Single<Unit>

    fun getCachedMemesByTitle(query: String): Single<List<Meme>?>
}