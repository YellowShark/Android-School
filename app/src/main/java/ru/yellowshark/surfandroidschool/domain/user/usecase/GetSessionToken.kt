package ru.yellowshark.surfandroidschool.domain.user.usecase

interface GetSessionToken {
    operator fun invoke(): String?
}