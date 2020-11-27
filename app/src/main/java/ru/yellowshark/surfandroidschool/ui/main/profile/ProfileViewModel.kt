package ru.yellowshark.surfandroidschool.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.yellowshark.surfandroidschool.data.network.NoConnectivityException
import ru.yellowshark.surfandroidschool.domain.Errors
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground

class ProfileViewModel(
    private val repository: Repository
) : BaseViewModel() {

    val viewState: LiveData<ViewState> get() = _viewState
    private val _viewState = MutableLiveData<ViewState>()
    val memesLiveData = MutableLiveData<List<Meme>>()
    val userInfo = repository.getLastSessionUserInfo()

    init {
        loadLocalMemes()
    }

    private fun loadLocalMemes() {
        disposables.add(
            repository.getLocalMemes()
                .runInBackground()
                .doOnSubscribe { _viewState.postValue(ViewState.Loading) }
                .subscribe { memes ->
                    memesLiveData.postValue(memes)
                    _viewState.postValue(ViewState.Success)
                }
        )
    }

    fun updateLike(meme: Meme) {
        disposables.add(
            repository.updateLocalMeme(meme).runInBackground().subscribe()
        )
    }

    fun logout() {
        disposables.add(
            repository.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        repository.forgetUser()
                        _viewState.postValue(ViewState.Destroy)
                    },
                    { t ->
                        val error = when (t) {
                            is NoConnectivityException -> ViewState.Error(Errors.NO_INTERNET)
                            else -> ViewState.Error(Errors.SERVER_ERROR)
                        }
                        _viewState.postValue(error)
                    }
                )
        )
    }
}