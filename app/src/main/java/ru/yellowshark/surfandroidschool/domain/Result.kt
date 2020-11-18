package ru.yellowshark.surfandroidschool.domain

sealed class Result<out T> {
    class Success<out T>(val data: T? = null) : Result<T>()
    class Error(val exception: Exception? = null) : Result<Exception>()
}