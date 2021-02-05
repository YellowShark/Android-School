package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Completable
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

interface CacheMemesUseCase {
    operator fun invoke(memes: List<Meme>): Completable
}