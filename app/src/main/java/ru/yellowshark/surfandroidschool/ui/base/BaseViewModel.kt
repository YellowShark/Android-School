package ru.yellowshark.surfandroidschool.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.yellowshark.surfandroidschool.data.network.NoConnectivityException
import ru.yellowshark.surfandroidschool.domain.Error
import ru.yellowshark.surfandroidschool.domain.ViewState

abstract class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    private fun handleError(t: Throwable): Error {
        return when (t) {
            is NoConnectivityException -> Error.NO_INTERNET
            else -> Error.SERVER_ERROR
        }
    }

    protected fun errorState(t: Throwable) = ViewState.Error(handleError(t))

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}