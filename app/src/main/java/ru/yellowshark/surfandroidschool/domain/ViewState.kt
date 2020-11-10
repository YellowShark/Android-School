package ru.yellowshark.surfandroidschool.domain

sealed class ViewState {
    object Loading : ViewState()
    object Success: ViewState()
    object Error: ViewState()
}