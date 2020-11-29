package ru.yellowshark.surfandroidschool.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.meme.usecase.GetLocalMemesUseCase
import ru.yellowshark.surfandroidschool.domain.meme.usecase.UpdateLocalMemeUseCase
import ru.yellowshark.surfandroidschool.domain.user.usecase.GetUserInfoUseCase
import ru.yellowshark.surfandroidschool.domain.user.usecase.LogoutUserUseCase
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.ui.base.ViewState
import ru.yellowshark.surfandroidschool.utils.runInBackground

class ProfileViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getLocalMemesUseCase: GetLocalMemesUseCase,
    private val updateLocalMemeUseCase: UpdateLocalMemeUseCase
) : BaseViewModel() {

    val viewState: LiveData<ViewState<List<Meme>>> get() = _viewState
    private val _viewState = MutableLiveData<ViewState<List<Meme>>>()

    init {
        loadLocalMemes()
    }

    fun getUserInfo() = getUserInfoUseCase()

    private fun loadLocalMemes() {
        disposables.add(
            getLocalMemesUseCase()
                .runInBackground()
                .doOnSubscribe { _viewState.postValue(ViewState.Loading()) }
                .subscribe { memes ->
                    _viewState.postValue(ViewState.Success(memes))
                }
        )
    }

    fun updateLike(meme: Meme) {
        disposables.add(
            updateLocalMemeUseCase(meme).runInBackground().subscribe()
        )
    }

    fun logout() {
        disposables.add(
            logoutUserUseCase()
                .runInBackground()
                .subscribe(
                    { _viewState.postValue(ViewState.Destroy()) },
                    { t -> _viewState.postValue(ViewState.Error(handleError(t))) }
                )
        )
    }
}