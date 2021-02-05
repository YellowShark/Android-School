package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Completable
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class CacheMemesUseCaseImpl(
    private val repository: Repository
) : CacheMemesUseCase {
    override fun invoke(memes: List<Meme>): Completable = repository.cacheMemes(memes)
}