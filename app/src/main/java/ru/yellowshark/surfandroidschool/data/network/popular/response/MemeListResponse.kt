package ru.yellowshark.surfandroidschool.data.network.popular.response

import ru.yellowshark.surfandroidschool.domain.Meme

class MemeListResponse : ArrayList<MemeResponse>() {

    fun toMemeList(): List<Meme> {
        val list = ArrayList<Meme>()
        this.forEach {
            list.add(it.toMeme())
        }
        return list
    }
}