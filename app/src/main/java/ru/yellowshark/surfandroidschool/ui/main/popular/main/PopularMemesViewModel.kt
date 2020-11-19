package ru.yellowshark.surfandroidschool.ui.main.popular.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.yellowshark.surfandroidschool.data.network.NoConnectivityException
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.utils.ERROR_NO_INTERNET
import java.util.concurrent.TimeUnit

class PopularMemesViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _memesListViewState = MutableLiveData<ViewState>()
    val memesListViewState: LiveData<ViewState> get() = _memesListViewState

    val memesLiveData = MutableLiveData<List<Meme>>()

    private val disposables = CompositeDisposable()

    init {
        requestPopularMemes()
    }

    fun getLastSessionUserInfo(): User? = repository.getLastSessionUserInfo()

    fun requestPopularMemes() {
        disposables.add(
            repository.fetchPopularMemes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _memesListViewState.value = ViewState.Loading }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(
                    { memes ->
                        _memesListViewState.postValue(ViewState.Success)
                        memesLiveData.postValue(memes)
                        repository.cacheMemes(memes)
                    },
                    { t ->
                        val error = when (t) {
                            is NoConnectivityException -> {
                               ViewState.Error(msg = ERROR_NO_INTERNET)
                            }
                            else -> {
                                ViewState.Error()
                            }
                        }
                        _memesListViewState.postValue(error)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
    }
}