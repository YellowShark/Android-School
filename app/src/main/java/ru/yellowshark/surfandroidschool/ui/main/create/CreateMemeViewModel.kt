package ru.yellowshark.surfandroidschool.ui.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.db.entity.EntityLocalMeme
import ru.yellowshark.surfandroidschool.data.repository.Repository

class CreateMemeViewModel(
    private val repository: Repository
) : ViewModel() {

    fun addMeme(entityMeme: EntityLocalMeme) {
        this.viewModelScope.launch(Dispatchers.IO) {
            repository.saveMeme(entityMeme)
        }
    }

}