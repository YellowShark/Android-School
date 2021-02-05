package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Completable
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

interface UpdateLocalMemeUseCase {
    operator fun invoke(meme: Meme): Completable
}