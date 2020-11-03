package ru.yellowshark.surfandroidschool.data.network.auth

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Success<out T>(val data: T): State<T>()
    object Error: State<Nothing>()
}