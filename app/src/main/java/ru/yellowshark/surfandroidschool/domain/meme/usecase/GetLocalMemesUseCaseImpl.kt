package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class GetLocalMemesUseCaseImpl(
    private val repository: Repository
) : GetLocalMemesUseCase {
    override fun invoke(): Single<List<Meme>?> = repository.getLocalMemes()
}