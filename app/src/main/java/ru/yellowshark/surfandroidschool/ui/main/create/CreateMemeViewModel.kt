package ru.yellowshark.surfandroidschool.ui.main.create

import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.domain.meme.usecase.SaveLocalMemeUseCase
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground

class CreateMemeViewModel(
    private val saveLocalMemeUseCase: SaveLocalMemeUseCase
) : BaseViewModel() {
    fun addMeme(meme: Meme) {
        disposables.add(saveLocalMemeUseCase(meme).runInBackground().subscribe())
    }
}