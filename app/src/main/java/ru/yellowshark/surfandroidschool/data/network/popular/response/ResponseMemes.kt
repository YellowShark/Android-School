package ru.yellowshark.surfandroidschool.data.network.popular.response

import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

class ResponseMemes : ArrayList<ResponseMeme>() {
    fun toDomainMemeList(): List<Meme> {
        return this.map { it.toDomainMeme() }
    }
}