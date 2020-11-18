package ru.yellowshark.surfandroidschool.domain

sealed class ViewState {
    object Loading : ViewState()
    object Success: ViewState()
    class Error(var msg: String? = null) : ViewState()
    object Destroy: ViewState()
}