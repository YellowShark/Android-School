package ru.yellowshark.surfandroidschool.ui.main.popular.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.meme.usecase.SearchMemesByTitleUseCase
import ru.yellowshark.surfandroidschool.domain.user.model.User
import ru.yellowshark.surfandroidschool.domain.user.usecase.GetUserInfoUseCase
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground

class MemeSearchFilterViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val searchMemesByTitleUseCase: SearchMemesByTitleUseCase
) : BaseViewModel() {

    private val _filterViewState = MutableLiveData<ViewState>()
    val filterViewState: LiveData<ViewState> get() = _filterViewState
    val filteredMemesLiveData = MutableLiveData<List<Meme>>()

    fun getUserInfo(): User = getUserInfoUseCase()

    fun searchMemes(title: String) {
        disposables.add(
            searchMemesByTitleUseCase(title)
                .runInBackground()
                .subscribe(
                    { memes ->
                        _filterViewState.postValue(ViewState.Success)
                        filteredMemesLiveData.postValue(memes)
                    },
                    { t -> _filterViewState.postValue(errorState(t)) }
                )
        )
    }
}