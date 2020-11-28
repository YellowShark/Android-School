package ru.yellowshark.surfandroidschool.domain.user.usecase

import ru.yellowshark.surfandroidschool.domain.repository.Repository

class GetSessionTokenImpl(
    private val repository: Repository
) : GetSessionToken {
    override fun invoke() = repository.getLastSessionToken()
}