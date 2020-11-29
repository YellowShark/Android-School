package ru.yellowshark.surfandroidschool.ui.main.popular.detail

import androidx.lifecycle.ViewModel
import ru.yellowshark.surfandroidschool.domain.user.usecase.GetUserInfoUseCase

class DetailMemeViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    fun getUserInfo() = getUserInfoUseCase()
}