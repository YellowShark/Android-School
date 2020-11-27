package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

interface CacheMemesUseCase {
    operator fun invoke(memes: List<Meme>): Single<Unit>
}