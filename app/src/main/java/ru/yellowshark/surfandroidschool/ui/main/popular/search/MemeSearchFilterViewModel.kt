package ru.yellowshark.surfandroidschool.ui.main.popular.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.meme.usecase.SearchMemesByTitleUseCase
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.ui.base.ViewState
import ru.yellowshark.surfandroidschool.utils.runInBackground

class MemeSearchFilterViewModel(
    private val searchMemesByTitleUseCase: SearchMemesByTitleUseCase
) : BaseViewModel() {

    private val _filterViewState = MutableLiveData<ViewState<List<Meme>>>()
    val filterViewState: LiveData<ViewState<List<Meme>>> get() = _filterViewState

    fun searchMemes(title: String) {
        disposables.add(
            searchMemesByTitleUseCase(title)
                .runInBackground()
                .subscribe(
                    { memes -> _filterViewState.postValue(ViewState.Success(memes)) },
                    { t -> _filterViewState.postValue(ViewState.Error(handleError(t))) }
                )
        )
    }
}