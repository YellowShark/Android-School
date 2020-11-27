package ru.yellowshark.surfandroidschool.domain.user.usecase

import ru.yellowshark.surfandroidschool.domain.user.model.User

interface GetUserInfoUseCase {
    operator fun invoke(): User
}