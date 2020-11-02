package ru.yellowshark.surfandroidschool.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.AuthState
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.repository.Repository

class AuthViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState<AuthResponse>>()
    val authState: LiveData<AuthState<AuthResponse>>
        get() = _authState


    fun login(login: String, password: String) {
        this.viewModelScope.launch(Dispatchers.IO) {
            _authState.postValue(AuthState.Loading)
            delay(500)
            val result = repository.login(AuthRequest(login, password))
            if (result != null) {
                _authState.postValue(AuthState.Success(result))
                sessionManager.saveUser(result)
            }
            else
                _authState.postValue(AuthState.Error)
        }
    }

    fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

}