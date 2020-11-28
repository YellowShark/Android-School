package ru.yellowshark.surfandroidschool.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.Error
import ru.yellowshark.surfandroidschool.domain.ViewState

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected val viewStateObserver = Observer<ViewState> { state ->
        when (state) {
            is ViewState.Loading -> showLoading()
            is ViewState.Success -> showContent()
            is ViewState.Error -> showError(state.error)
            is ViewState.Destroy -> destroyView()
        }
        doFinallyAfterEvent()
    }

    protected abstract fun showError(error: Error)

    protected abstract fun showContent()

    protected abstract fun showLoading()

    protected open fun doFinallyAfterEvent() {}

    protected open fun destroyView() {}

    protected fun getErrorMessageText(error: Error) = when(error) {
        Error.SERVER_ERROR -> getString(R.string.error_fail_load_msg)
        Error.NO_INTERNET -> getString(R.string.error_no_internet)
        Error.NO_RESULTS -> getString(R.string.error_nothing_found)
    }
}