package ru.yellowshark.surfandroidschool.domain

sealed class ViewState {
    object Loading : ViewState()
    object Success: ViewState()
    class Error(var error: Errors) : ViewState()
    object Destroy: ViewState()
}