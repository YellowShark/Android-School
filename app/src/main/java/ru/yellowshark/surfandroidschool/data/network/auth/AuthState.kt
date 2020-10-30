package ru.yellowshark.surfandroidschool.data.network.auth

sealed class AuthState<out T> {
    object Loading : AuthState<Nothing>()
    data class Success<out T>(val data: T): AuthState<T>()
    object Error: AuthState<Nothing>()
}