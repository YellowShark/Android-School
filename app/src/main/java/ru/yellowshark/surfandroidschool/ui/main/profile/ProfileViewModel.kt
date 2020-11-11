package ru.yellowshark.surfandroidschool.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.ViewState

class ProfileViewModel(
    private val repository: Repository
) : ViewModel() {
    val viewState: LiveData<ViewState> get() = _viewState
    private val _viewState = MutableLiveData<ViewState>()
    val memesLiveData = MutableLiveData<List<Meme>>()

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

    val userInfo = repository.getLastSessionUserInfo()

}