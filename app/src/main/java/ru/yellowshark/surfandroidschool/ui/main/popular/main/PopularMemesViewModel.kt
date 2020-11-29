package ru.yellowshark.surfandroidschool.ui.main.popular.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.meme.usecase.CacheMemesUseCase
import ru.yellowshark.surfandroidschool.domain.meme.usecase.GetPopularMemesUseCase
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground
import java.util.concurrent.TimeUnit

class PopularMemesViewModel(
    private val getPopularMemesUseCase: GetPopularMemesUseCase,
    private val cacheMemesUseCase: CacheMemesUseCase,
) : BaseViewModel() {

    private val _memesListViewState = MutableLiveData<ViewState>()
    val memesListViewState: LiveData<ViewState> get() = _memesListViewState
    val memesLiveData = MutableLiveData<List<Meme>>()

    init {
        requestPopularMemes()
    }

    fun requestPopularMemes() {
        disposables.add(
            getPopularMemesUseCase()
                .runInBackground()
                .doOnSubscribe { _memesListViewState.value = ViewState.Loading }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(
                    { memes -> postSuccess(memes) },
                    { t -> _memesListViewState.postValue(errorState(t)) }
                )
        )
    }

    private fun postSuccess(memes: List<Meme>) {
        _memesListViewState.postValue(ViewState.Success)
        memesLiveData.postValue(memes)
        cacheMemes(memes)
    }

    private fun cacheMemes(memes: List<Meme>) {
        disposables.add(
            cacheMemesUseCase(memes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }
}