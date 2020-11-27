package ru.yellowshark.surfandroidschool.data.network.popular.response

import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

class ResponseMemeList : ArrayList<ResponseMeme>() {

    fun fromResponseMemeListToDomainMemeList(): List<Meme> {
        return this.map { it.fromResponseMemeToDomainMeme() }
    }
}