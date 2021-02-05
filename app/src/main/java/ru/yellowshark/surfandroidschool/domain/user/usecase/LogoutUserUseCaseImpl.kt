package ru.yellowshark.surfandroidschool.domain.user.usecase

import io.reactivex.Completable
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class LogoutUserUseCaseImpl(
    private val repository: Repository
) : LogoutUserUseCase {
    override fun invoke(): Completable {
        return repository.logout().doOnComplete { repository.forgetUser() }
    }
}