package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class GetPopularMemesUseCaseImpl(
    private val repository: Repository
) : GetPopularMemesUseCase {
    override fun invoke(): Single<List<Meme>> = repository.getPopularMemes()
}