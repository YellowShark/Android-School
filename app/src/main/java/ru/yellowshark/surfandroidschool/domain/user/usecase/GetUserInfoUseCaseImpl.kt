package ru.yellowshark.surfandroidschool.domain.user.usecase

import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.model.User

class GetUserInfoUseCaseImpl(
    private val repository: Repository
) : GetUserInfoUseCase {
    override fun invoke(): User = repository.getLastSessionUserInfo()
}