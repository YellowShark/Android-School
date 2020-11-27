package ru.yellowshark.surfandroidschool.ui.main.popular.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yellowshark.surfandroidschool.domain.Errors
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.model.User
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground

class MemeSearchFilterViewModel(
    private val repository: Repository
) : BaseViewModel() {

    private val _filterViewState = MutableLiveData<ViewState>()
    val filterViewState: LiveData<ViewState> get() = _filterViewState
    val filteredMemesLiveData = MutableLiveData<List<Meme>>()

    fun searchMemes(title: String) {
        disposables.add(
            repository.getCachedMemesByTitle(title)
                .runInBackground()
                .subscribe(
                    { memes ->
                        _filterViewState.postValue(ViewState.Success)
                        filteredMemesLiveData.postValue(memes)
                    },
                    { _filterViewState.postValue(ViewState.Error(Errors.SERVER_ERROR)) }
                )
        )
    }

    fun getLastSessionUserInfo(): User = repository.getLastSessionUserInfo()
}