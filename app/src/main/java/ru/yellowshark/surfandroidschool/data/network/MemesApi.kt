package ru.yellowshark.surfandroidschool.data.network

import retrofit2.Response
import retrofit2.http.GET
import ru.yellowshark.surfandroidschool.data.network.popular.response.ResponseMemeList

interface MemesApi {
    @GET("memes")
    suspend fun getPopularMemes(): Response<ResponseMemeList>
}