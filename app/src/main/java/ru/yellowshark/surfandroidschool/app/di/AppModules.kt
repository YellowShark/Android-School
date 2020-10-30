package ru.yellowshark.surfandroidschool.app.di

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.repository.Repository
import ru.yellowshark.surfandroidschool.ui.auth.AuthViewModel

class AppModules {

    val viewModelModule = module {
        viewModel { AuthViewModel(get()) }
    }

    val repositoryModule = module {
        single { Repository(get()) }
    }

    val networkModule = module {
        single { MemesApi.getInstance() }
    }

}