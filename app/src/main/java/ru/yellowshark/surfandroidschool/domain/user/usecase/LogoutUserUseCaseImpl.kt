package ru.yellowshark.surfandroidschool.domain.user.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class LogoutUserUseCaseImpl(
    private val repository: Repository
) : LogoutUserUseCase {
    override fun invoke(): Single<Unit> {
        return repository.logout().doOnSuccess { repository.forgetUser() }
    }
}