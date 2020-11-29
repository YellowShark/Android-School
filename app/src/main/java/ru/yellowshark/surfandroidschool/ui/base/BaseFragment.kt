package ru.yellowshark.surfandroidschool.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.ResponseError
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected val viewStateObserver = Observer<ViewState<List<Meme>>> { state ->
        when (state) {
            is ViewState.Loading -> showLoading()
            is ViewState.Success<List<Meme>> -> showContent(state.data)
            is ViewState.Error -> showError(state.error)
            is ViewState.Destroy -> destroyView()
        }
        doFinallyAfterEvent()
    }

    protected abstract fun showError(error: ResponseError)

    protected abstract fun showContent(data: List<Meme>?)

    protected abstract fun showLoading()

    protected open fun doFinallyAfterEvent() {}

    protected open fun destroyView() {}

    protected fun getErrorMessageText(error: ResponseError) = when(error) {
        ResponseError.SERVER_ERROR -> getString(R.string.error_fail_load_msg)
        ResponseError.NO_INTERNET -> getString(R.string.error_no_internet)
        ResponseError.NO_RESULTS -> getString(R.string.error_nothing_found)
    }
}