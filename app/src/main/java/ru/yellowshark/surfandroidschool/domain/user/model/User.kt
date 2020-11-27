package ru.yellowshark.surfandroidschool.domain.user.model

import ru.yellowshark.surfandroidschool.utils.BASE_USER_PHOTO

data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
    val userDescription: String,
    val photoUrl: String = BASE_USER_PHOTO
) {
    companion object {
        val EMPTY = User("", "", "", "", "")
    }
}