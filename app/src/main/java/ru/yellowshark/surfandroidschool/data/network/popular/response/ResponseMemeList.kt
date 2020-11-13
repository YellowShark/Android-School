package ru.yellowshark.surfandroidschool.data.network.popular.response

import ru.yellowshark.surfandroidschool.domain.Meme

class ResponseMemeList : ArrayList<ResponseMeme>() {

    fun fromResponseMemeListToDomainMemeList(): List<Meme> {
        return this.map { it.fromResponseMemeToDomainMeme() }
    }
}