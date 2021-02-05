package ru.yellowshark.surfandroidschool.domain.user.usecase

import io.reactivex.Completable

interface LogoutUserUseCase {
    operator fun invoke(): Completable
}