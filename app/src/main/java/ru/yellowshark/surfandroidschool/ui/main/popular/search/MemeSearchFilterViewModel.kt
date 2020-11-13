package ru.yellowshark.surfandroidschool.ui.main.popular.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.domain.ViewState

class MemeSearchFilterViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _filterViewState = MutableLiveData<ViewState>()
    val filterViewState: LiveData<ViewState> get() = _filterViewState
    val filteredMemesLiveData = MutableLiveData<List<Meme>>()

    fun searchMemes(title: String) {
        this.viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCachedMemesByTitle(title)
            if (result.isNotEmpty()) {
                _filterViewState.postValue(ViewState.Success)
                filteredMemesLiveData.postValue(result)
            } else {
                _filterViewState.postValue(ViewState.Error)
            }
        }
    }
    fun getLastSessionUserInfo(): User? = repository.getLastSessionUserInfo()

}