package ru.yellowshark.surfandroidschool.domain.user.usecase

import io.reactivex.Single
import ru.yellowshark.surfandroidschool.domain.repository.Repository

class LoginUserUseCaseImpl(
    private val repository: Repository,
    private val saveUserUseCase: SaveUserUseCase
) : LoginUserUseCase {
    override fun invoke(login: String, password: String): Single<Unit> =
        repository.login(login, password)
            .map { saveUserUseCase(it.accessToken, it.userInfo.toDomainUser()) }
}