package ru.yellowshark.surfandroidschool.domain.user.usecase

import io.reactivex.Single

interface LogoutUserUseCase {
    operator fun invoke(): Single<Unit>
}