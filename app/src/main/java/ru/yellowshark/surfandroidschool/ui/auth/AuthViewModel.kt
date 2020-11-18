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
import ru.yellowshark.surfandroidschool.internal.NoConnectivityException

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
            else when ((result as Result.Error).exception) {
                is NoConnectivityException -> _authState.postValue(ViewState.Error(msg = "Нет подключения к интернету"))
                else -> _authState.postValue(ViewState.Error())
            }
        }
    }

    fun getLastSessionToken(): String? = repository.getLastSessionToken()

}