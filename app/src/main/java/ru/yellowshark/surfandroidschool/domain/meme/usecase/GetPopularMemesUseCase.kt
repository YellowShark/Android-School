package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

interface GetPopularMemesUseCase {
    operator fun invoke(): Single<List<Meme>>
}