package ru.yellowshark.surfandroidschool.ui.main.profile

import androidx.lifecycle.ViewModel
import ru.yellowshark.surfandroidschool.data.repository.Repository

class ProfileViewModel(
    private val repository: Repository
) : ViewModel() {

    val userInfo = repository.getLastSessionUserInfo()

}