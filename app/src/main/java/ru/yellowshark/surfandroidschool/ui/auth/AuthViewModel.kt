package ru.yellowshark.surfandroidschool.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yellowshark.surfandroidschool.domain.user.usecase.GetSessionToken
import ru.yellowshark.surfandroidschool.domain.user.usecase.LoginUserUseCase
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.ui.base.ViewState
import ru.yellowshark.surfandroidschool.utils.runInBackground

class AuthViewModel(
    private val getSessionToken: GetSessionToken,
    private val loginUserUseCase: LoginUserUseCase,
) : BaseViewModel() {

    private val _authState = MutableLiveData<ViewState<Unit>>()
    val authViewState: LiveData<ViewState<Unit>>
        get() = _authState

    fun getLastSessionToken(): String? = getSessionToken()

    fun login(login: String, password: String) {
        disposables.add(
            loginUserUseCase(login, password)
                .runInBackground()
                .doOnSubscribe { _authState.value = ViewState.Loading() }
                .subscribe(
                    { _authState.postValue(ViewState.Success()) },
                    { t -> _authState.postValue(ViewState.Error(handleError(t))) }
                )
        )
    }
}