package ru.yellowshark.surfandroidschool.ui.main.popular.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.utils.getMessage
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
            repository.getPopularMemes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _memesListViewState.value = ViewState.Loading }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(
                    { memes -> postSuccess(memes) },
                    { t -> _memesListViewState.postValue(ViewState.Error(t.getMessage())) }
                )
        )
    }

    private fun postSuccess(memes: List<Meme>) {
        _memesListViewState.postValue(ViewState.Success)
        memesLiveData.postValue(memes)
        repository.cacheMemes(memes)
    }

    override fun onCleared() {
        disposables.clear()
    }
}