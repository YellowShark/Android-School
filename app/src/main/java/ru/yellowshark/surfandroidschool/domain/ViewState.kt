package ru.yellowshark.surfandroidschool.domain

sealed class ViewState {
    object Loading : ViewState()
    object Success: ViewState()
    class Error(var error: ru.yellowshark.surfandroidschool.domain.Error) : ViewState()
    object Destroy: ViewState()
}