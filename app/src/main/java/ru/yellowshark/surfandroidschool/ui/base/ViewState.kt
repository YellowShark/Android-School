package ru.yellowshark.surfandroidschool.ui.base

import ru.yellowshark.surfandroidschool.domain.ResponseError

sealed class ViewState<T> {
    class Loading<T> : ViewState<T>()
    data class Success<T>(val data: T? = null): ViewState<T>()
    data class Error<T>(var error: ResponseError) : ViewState<T>()
    class Destroy<T>: ViewState<T>()
}