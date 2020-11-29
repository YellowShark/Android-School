package ru.yellowshark.surfandroidschool.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.yellowshark.surfandroidschool.domain.Error
import ru.yellowshark.surfandroidschool.domain.NoConnectivityException
import ru.yellowshark.surfandroidschool.domain.NothingFoundException
import ru.yellowshark.surfandroidschool.domain.ViewState

abstract class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    protected fun errorState(t: Throwable) = ViewState.Error(handleError(t))

    private fun handleError(t: Throwable): Error {
        return when (t) {
            is NoConnectivityException -> Error.NO_INTERNET
            is NothingFoundException -> Error.NO_RESULTS
            else -> Error.SERVER_ERROR
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}