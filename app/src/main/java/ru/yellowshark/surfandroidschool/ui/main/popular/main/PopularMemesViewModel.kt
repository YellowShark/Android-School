package ru.yellowshark.surfandroidschool.ui.main.popular.main

import android.util.Log
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

class PopularMemesViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _memesListViewState = MutableLiveData<ViewState>()
    val memesListViewState: LiveData<ViewState>
        get() = _memesListViewState

    val memes = MutableLiveData<List<Meme>>()

    fun requestPopularMemes() {
        this.viewModelScope.launch(Dispatchers.IO) {
            _memesListViewState.postValue(ViewState.Loading)
            delay(500)
            val result = repository.fetchPopularMemes()
            Log.d("TAG", "requestPopularMemes: ${result.toString()}")
            if (result is Result.Success) {
                _memesListViewState.postValue(ViewState.Success)
                memes.postValue(result.data)
            }
            else
                _memesListViewState.postValue(ViewState.Error)
        }
    }

    fun getLastSessionUserInfo(): User? = repository.getLastSessionUserInfo()
}