package ru.yellowshark.surfandroidschool.app.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.yellowshark.surfandroidschool.data.db.MemesDatabase
import ru.yellowshark.surfandroidschool.data.network.AuthApi
import ru.yellowshark.surfandroidschool.data.network.ConnectivityInterceptor
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.repository.RepositoryImpl
import ru.yellowshark.surfandroidschool.domain.meme.usecase.*
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.usecase.*
import ru.yellowshark.surfandroidschool.ui.auth.AuthViewModel
import ru.yellowshark.surfandroidschool.ui.main.create.CreateMemeViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.main.PopularMemesViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.search.MemeSearchFilterViewModel
import ru.yellowshark.surfandroidschool.ui.main.profile.ProfileViewModel
import ru.yellowshark.surfandroidschool.utils.BASE_URL

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { PopularMemesViewModel(get(), get(), get()) }
    viewModel { MemeSearchFilterViewModel(get(), get()) }
    viewModel { CreateMemeViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
}

val repositoryModule = module {
    single<Repository> { RepositoryImpl(get(), get(), get(), get()) }
}

val useCasesModule = module {
    factory<GetPopularMemesUseCase> { GetPopularMemesUseCaseImpl(get()) }
    factory<SearchMemesByTitleUseCase> { SearchMemesByTitleUseCaseImpl(get()) }
    factory<CacheMemesUseCase> { CacheMemesUseCaseImpl(get()) }

    factory<GetLocalMemesUseCase> { GetLocalMemesUseCaseImpl(get()) }
    factory<SaveLocalMemeUseCase> { SaveLocalMemeUseCaseImpl(get()) }
    factory<UpdateLocalMemeUseCase> { UpdateLocalMemeUseCaseImpl(get()) }

    factory<GetUserInfoUseCase> { GetUserInfoUseCaseImpl(get()) }
    factory<GetSessionToken> { GetSessionTokenImpl(get()) }
    factory<LoginUserUseCase> { LoginUserUseCaseImpl(get()) }
    factory<LogoutUserUseCase> { LogoutUserUseCaseImpl(get()) }
}

val sessionModule = module {
    single { SessionManager(androidApplication(), get()) }
}

val daoModule = module {
    single { MemesDatabase(androidContext()).memesDao() }
}

val networkModule = module {
    factory { ConnectivityInterceptor(androidContext()) }
    factory { provideOkHttpClient(get()) }
    single { Gson() }
    single { provideRetrofit(get(), get()) }
    factory { provideMemesApi(get()) }
    factory { provideAuthApi(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun provideOkHttpClient(connectivityInterceptor: ConnectivityInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(connectivityInterceptor)
        .build()
}

fun provideMemesApi(retrofit: Retrofit): MemesApi = retrofit.create(MemesApi::class.java)

fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)