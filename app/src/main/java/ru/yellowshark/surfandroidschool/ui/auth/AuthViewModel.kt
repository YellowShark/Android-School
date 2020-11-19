package ru.yellowshark.surfandroidschool.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.yellowshark.surfandroidschool.data.network.NoConnectivityException
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.utils.ERROR_NO_INTERNET

class AuthViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _authState = MutableLiveData<ViewState>()
    val authViewState: LiveData<ViewState>
        get() = _authState

    private val disposables = CompositeDisposable()

    fun getLastSessionToken(): String? = repository.getLastSessionToken()

    fun login(login: String, password: String) {
        disposables.add(
            repository.login(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _authState.value = ViewState.Loading }
                .subscribe(
                    { _authState.postValue(ViewState.Success) },
                    { t ->
                        val error = when (t) {
                            is NoConnectivityException -> ViewState.Error(msg = ERROR_NO_INTERNET)
                            else -> ViewState.Error()
                        }
                        _authState.postValue(error)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
    }
}