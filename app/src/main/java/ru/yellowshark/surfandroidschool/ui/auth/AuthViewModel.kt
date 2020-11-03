package ru.yellowshark.surfandroidschool.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.State
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.repository.Repository

class AuthViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableLiveData<State<AuthResponse>>()
    val authState: LiveData<State<AuthResponse>>
        get() = _authState


    fun login(login: String, password: String) {
        this.viewModelScope.launch(Dispatchers.IO) {
            _authState.postValue(State.Loading)
            delay(500)
            val result = repository.login(AuthRequest(login, password))
            if (result != null) {
                _authState.postValue(State.Success(result))
                sessionManager.saveUser(result)
            }
            else
                _authState.postValue(State.Error)
        }
    }

    fun getLastSessionToken(): String? = sessionManager.fetchAuthToken()

}