package ru.yellowshark.surfandroidschool.app.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.yellowshark.surfandroidschool.data.network.AuthApi
import ru.yellowshark.surfandroidschool.data.network.ConnectivityInterceptor
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.utils.BASE_URL

val networkModule = module {
    factory { ConnectivityInterceptor(androidContext()) }
    factory { provideOkHttpClient(get()) }
    single { Gson() }
    single { provideRetrofit(get(), get()) }
    factory { provideMemesApi(get()) }
    factory { provideAuthApi(get()) }
}

val sessionModule = module {
    single { SessionManager(androidApplication(), get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

private fun provideOkHttpClient(connectivityInterceptor: ConnectivityInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(connectivityInterceptor)
        .build()
}

private fun provideMemesApi(retrofit: Retrofit): MemesApi = retrofit.create(MemesApi::class.java)

private fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)