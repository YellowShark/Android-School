package ru.yellowshark.surfandroidschool.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.network.popular.response.ResponseMemeList
import ru.yellowshark.surfandroidschool.utils.BASE_URL

interface MemesApi {
    // TODO AuthApi
    @POST("auth/login")
    suspend fun userAuth(@Body request: AuthRequest): Response<AuthResponse>

    @GET("memes")
    suspend fun getPopularMemes(): Response<ResponseMemeList>

    @POST("auth/logout")
    suspend fun userLogout(): Response<*>
    // TODO create with koin like in example https://medium.com/@harmittaa/retrofit-2-6-0-with-koin-and-coroutines-4ff45a4792fc
    companion object {
        var INSTANCE: MemesApi? = null
        fun getInstance(connectivityInterceptor: ConnectivityInterceptor): MemesApi {
            if (INSTANCE == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(connectivityInterceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
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