package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

interface UpdateLocalMemeUseCase {
    operator fun invoke(meme: Meme): Single<Unit>
}