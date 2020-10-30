package ru.yellowshark.surfandroidschool.data.network

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import ru.yellowshark.surfandroidschool.BuildConfig
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse

interface MemesApi {

    @POST("auth/login")
    fun userAuth(@Body request: AuthRequest): Call<AuthResponse>

    companion object {
        var INSTANCE: MemesApi? = null
        fun getInstance(): MemesApi {

            if (INSTANCE == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

                return retrofit.create(MemesApi::class.java)
            }

            return INSTANCE!!
        }
    }

}