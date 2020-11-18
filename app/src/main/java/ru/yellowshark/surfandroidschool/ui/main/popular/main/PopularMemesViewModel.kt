package ru.yellowshark.surfandroidschool.ui.main.popular.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.Result
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.internal.NoConnectivityException
import ru.yellowshark.surfandroidschool.internal.NothingFoundException

class PopularMemesViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _memesListViewState = MutableLiveData<ViewState>()
    val memesListViewState: LiveData<ViewState> get() = _memesListViewState

    val memesLiveData = MutableLiveData<List<Meme>>()

    init {
        requestPopularMemes()
    }

    fun requestPopularMemes() {
        this.viewModelScope.launch(Dispatchers.IO) {
            _memesListViewState.postValue(ViewState.Loading)
            delay(500)
            val result = repository.fetchPopularMemes()
            if (result is Result.Success) {
                _memesListViewState.postValue(ViewState.Success)
                memesLiveData.postValue(result.data as List<Meme>)
                repository.cacheMemes(result.data)
            } else when ((result as Result.Error).exception) {
                is NoConnectivityException -> {
                    _memesListViewState.postValue(ViewState.Error(msg = "Нет подключения к интернету"))
                }
                is NothingFoundException -> {
                    _memesListViewState.postValue(ViewState.Error())
                }
                else -> {
                    _memesListViewState.postValue(ViewState.Error())
                }
            }
        }
    }

    fun getLastSessionUserInfo(): User? = repository.getLastSessionUserInfo()
}