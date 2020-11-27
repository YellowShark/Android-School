package ru.yellowshark.surfandroidschool.ui.main.create

import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.ui.base.BaseViewModel
import ru.yellowshark.surfandroidschool.utils.runInBackground

class CreateMemeViewModel(
    private val repository: Repository
) : BaseViewModel() {
    fun addMeme(entityMeme: EntityLocalMeme) {
        disposables.add(repository.saveMeme(entityMeme).runInBackground().subscribe())
    }
}