package ru.yellowshark.surfandroidschool.ui.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.yellowshark.surfandroidschool.data.db.entity.EntityMeme
import ru.yellowshark.surfandroidschool.data.repository.Repository

class CreateMemeViewModel(
    private val repository: Repository
) : ViewModel() {

    fun addMeme(entityMeme: EntityMeme) {
        this.viewModelScope.launch(Dispatchers.IO) {
            repository.saveMeme(entityMeme)
        }
    }

}