package ru.yellowshark.surfandroidschool.app.di

import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.ui.auth.AuthViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.PopularMemesViewModel

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { PopularMemesViewModel(get(), get()) }
}

val repositoryModule = module {
    single { Repository(get()) }
}

val networkModule = module {
    single { MemesApi.getInstance() }
}

val sessionModule = module {
    single { SessionManager(androidApplication()) }
}