package ru.yellowshark.surfandroidschool.domain.user.usecase

import ru.yellowshark.surfandroidschool.domain.user.model.User

interface SaveUserUseCase {
    operator fun invoke(token: String, user: User)
}