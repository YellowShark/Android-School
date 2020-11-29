package ru.yellowshark.surfandroidschool.domain.meme.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.NothingFoundException
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class SearchMemesByTitleUseCaseImpl(
    private val repository: Repository
) : SearchMemesByTitleUseCase {
    override fun invoke(query: String): Single<List<Meme>?> =
       repository.getCachedMemesByTitle(query).map {
           if (it.isEmpty()) throw NothingFoundException()
           else it
       }

}