package ru.yellowshark.surfandroidschool.domain

sealed class Result<out T>() {
    class Success<out T>(val data: T? = null): Result<T>()
    object Error: Result<Nothing>()
}