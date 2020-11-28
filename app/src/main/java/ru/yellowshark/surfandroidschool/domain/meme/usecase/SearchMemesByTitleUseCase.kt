package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

interface SearchMemesByTitleUseCase {
    operator fun invoke(query: String): Single<List<Meme>?>
}