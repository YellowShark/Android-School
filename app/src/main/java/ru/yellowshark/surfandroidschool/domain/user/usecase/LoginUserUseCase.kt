package ru.yellowshark.surfandroidschool.domain.user.usecase

import io.reactivex.Single

interface LoginUserUseCase {
    operator fun invoke(login: String, password: String): Single<Unit>
}