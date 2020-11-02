package ru.yellowshark.surfandroidschool.data.network

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.network.popular.response.Meme
import ru.yellowshark.surfandroidschool.utils.BASE_URL

interface MemesApi {

    @POST("auth/login")
    suspend fun userAuth(@Body request: AuthRequest): Response<AuthResponse>

    @GET("memes")
    fun getPopularMemes(): Call<List<Meme>>

    companion object {
        var INSTANCE: MemesApi? = null
        fun getInstance(): MemesApi {

            if (INSTANCE == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

                return retrofit.create(MemesApi::class.java)
            }

            return INSTANCE!!
        }
    }

}