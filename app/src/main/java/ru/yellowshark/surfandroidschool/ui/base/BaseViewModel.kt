package ru.yellowshark.surfandroidschool.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.yellowshark.surfandroidschool.domain.NoConnectivityException
import ru.yellowshark.surfandroidschool.domain.NothingFoundException
import ru.yellowshark.surfandroidschool.domain.ResponseError

abstract class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    protected fun handleError(t: Throwable): ResponseError {
        return when (t) {
            is NoConnectivityException -> ResponseError.NO_INTERNET
            is NothingFoundException -> ResponseError.NO_RESULTS
            else -> ResponseError.SERVER_ERROR
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}