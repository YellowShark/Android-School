package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class UpdateLocalMemeUseCaseImpl(
    private val repository: Repository
) : UpdateLocalMemeUseCase {
    override fun invoke(meme: Meme): Single<Unit> = repository.updateLocalMeme(meme)
}