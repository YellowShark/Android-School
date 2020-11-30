package ru.yellowshark.surfandroidschool.app.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.yellowshark.surfandroidschool.data.network.AuthApi
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.domain.NoConnectivityException
import ru.yellowshark.surfandroidschool.utils.BASE_URL

val networkModule = module {
    factory { provideInterceptor(androidContext()) }
    factory { provideOkHttpClient(get()) }
    single { Gson() }
    single { provideRetrofit(get(), get()) }
    factory { provideMemesApi(get()) }
    factory { provideAuthApi(get()) }
}

val sessionModule = module {
    single { SessionManager(androidApplication()) }
}

private fun provideInterceptor(appContext: Context): Interceptor {
    fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    return Interceptor { chain ->
        if (!isOnline())
            throw NoConnectivityException()
        return@Interceptor chain.proceed(chain.request())
    }
}

private fun provideOkHttpClient(connectivityInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(connectivityInterceptor)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

private fun provideMemesApi(retrofit: Retrofit): MemesApi = retrofit.create(MemesApi::class.java)

private fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)