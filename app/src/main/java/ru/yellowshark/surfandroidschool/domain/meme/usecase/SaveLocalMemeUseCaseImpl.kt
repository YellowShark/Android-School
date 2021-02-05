package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Completable
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class SaveLocalMemeUseCaseImpl(
    private val repository: Repository
) : SaveLocalMemeUseCase {
    override fun invoke(meme: Meme): Completable = repository.saveMeme(meme)
}