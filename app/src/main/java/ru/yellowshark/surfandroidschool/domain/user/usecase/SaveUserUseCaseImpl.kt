package ru.yellowshark.surfandroidschool.domain.user.usecase

import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.model.User

class SaveUserUseCaseImpl(
    private val repository: Repository
) : SaveUserUseCase {
    override fun invoke(token: String, user: User) = repository.saveUser(token, user)
}