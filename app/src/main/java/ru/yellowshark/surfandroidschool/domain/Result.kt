package ru.yellowshark.surfandroidschool.domain

sealed class Result {
    object Success: Result()
    object Error: Result()
}