package ru.yellowshark.surfandroidschool.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yellowshark.surfandroidschool.data.network.NoConnectivityException
import ru.yellowshark.surfandroidschool.domain.Errors
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground

class AuthViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _authState = MutableLiveData<ViewState>()
    val authViewState: LiveData<ViewState>
        get() = _authState

    fun getLastSessionToken(): String = repository.getLastSessionToken()

    fun login(login: String, password: String) {
        disposables.add(
            repository.login(login, password)
                .runInBackground()
                .doOnSubscribe { _authState.value = ViewState.Loading }
                .subscribe(
                    { _authState.postValue(ViewState.Success) },
                    { t ->
                        val error = when (t) {
                            is NoConnectivityException -> ViewState.Error(Errors.NO_INTERNET)
                            else -> ViewState.Error(Errors.SERVER_ERROR)
                        }
                        _authState.postValue(error)
                    }
                )
        )
    }
}