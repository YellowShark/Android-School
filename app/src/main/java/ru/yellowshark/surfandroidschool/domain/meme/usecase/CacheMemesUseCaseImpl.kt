package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class CacheMemesUseCaseImpl(
    private val repository: Repository
) : CacheMemesUseCase {
    override fun invoke(memes: List<Meme>): Single<Unit> = repository.cacheMemes(memes)
}