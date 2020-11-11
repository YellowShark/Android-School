package ru.yellowshark.surfandroidschool.app.di

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.yellowshark.surfandroidschool.data.db.MemesDatabase
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.SessionManager
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.ui.auth.AuthViewModel
import ru.yellowshark.surfandroidschool.ui.main.create.CreateMemeViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.main.PopularMemesViewModel
import ru.yellowshark.surfandroidschool.ui.main.profile.ProfileViewModel

val viewModelsModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { PopularMemesViewModel(get()) }
    viewModel { CreateMemeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}

val repositoryModule = module {
    single { Repository(get(), get(), get()) }
}

val networkModule = module {
    single { MemesApi.getInstance() }
}

val sessionModule = module {
    single { SessionManager(androidApplication()) }
}

val daoModule = module {
    single { MemesDatabase.invoke(androidContext()).memesDao() }
}