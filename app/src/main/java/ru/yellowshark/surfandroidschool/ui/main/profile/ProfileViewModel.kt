package ru.yellowshark.surfandroidschool.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.network.NoConnectivityException
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.utils.ERROR_NO_INTERNET

class ProfileViewModel(
    private val repository: Repository
) : ViewModel() {

    val viewState: LiveData<ViewState> get() = _viewState
    private val _viewState = MutableLiveData<ViewState>()
    val memesLiveData = MutableLiveData<List<Meme>>()
    val userInfo get() = repository.getLastSessionUserInfo()
    private val disposables = CompositeDisposable()

    init {
        loadLocalMemes()
    }

    private fun loadLocalMemes() {
        this.viewModelScope.launch(Dispatchers.IO) {
            _viewState.postValue(ViewState.Loading)
            delay(500)
            val result = repository.getLocalMemes()
            memesLiveData.postValue(result)
            _viewState.postValue(ViewState.Success)
        }
    }

    fun updateLike(meme: Meme) {
        this.viewModelScope.launch(Dispatchers.IO) {
            repository.updateLocalMeme(meme)
        }
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
                            is NoConnectivityException -> ViewState.Error(msg = ERROR_NO_INTERNET)
                            else -> ViewState.Error()
                        }
                        _viewState.postValue(error)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
    }
}