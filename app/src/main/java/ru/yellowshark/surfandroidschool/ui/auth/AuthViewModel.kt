package ru.yellowshark.surfandroidschool.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Result
import ru.yellowshark.surfandroidschool.domain.ViewState

class AuthViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _authState = MutableLiveData<ViewState>()
    val authViewState: LiveData<ViewState>
        get() = _authState

    fun login(login: String, password: String) {
        this.viewModelScope.launch(Dispatchers.IO) {
            _authState.postValue(ViewState.Loading)
            delay(500)
            val result = repository.login(login, password)
            if (result is Result.Success<*>)
                _authState.postValue(ViewState.Success)
            else
                _authState.postValue(ViewState.Error)
        }
    }

    fun getLastSessionToken(): String? = repository.getLastSessionToken()

}