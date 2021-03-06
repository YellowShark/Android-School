package ru.yellowshark.surfandroidschool.data.network

import io.reactivex.Single
import retrofit2.http.GET
import ru.yellowshark.surfandroidschool.data.network.popular.response.ResponseMemes

interface MemesApi {
    @GET("memes")
    fun getPopularMemes(): Single<ResponseMemes>
}