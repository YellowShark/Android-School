package ru.yellowshark.surfandroidschool.ui.main.popular

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.network.auth.State
import ru.yellowshark.surfandroidschool.data.network.auth.response.UserInfo
import ru.yellowshark.surfandroidschool.data.network.popular.response.Meme
import ru.yellowshark.surfandroidschool.data.repository.Repository

class PopularMemesViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _memesListState = MutableLiveData<State<List<Meme>>>()
    val memesListState: LiveData<State<List<Meme>>>
        get() = _memesListState

    fun requestPopularMemes() {
        this.viewModelScope.launch(Dispatchers.IO) {
            _memesListState.postValue(State.Loading)
            delay(500)
            val result = repository.fetchPopularMemes()
            Log.d("TAG", "requestPopularMemes: $result")
            if (result != null)
                _memesListState.postValue(State.Success(result))
            else
                _memesListState.postValue(State.Error)
        }
    }

    fun getLastSessionUserInfo(): UserInfo? = sessionManager.fetchUserInfo()
}