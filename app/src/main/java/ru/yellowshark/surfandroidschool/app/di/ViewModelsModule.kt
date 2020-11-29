package ru.yellowshark.surfandroidschool.app.di

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.yellowshark.surfandroidschool.ui.auth.AuthViewModel
import ru.yellowshark.surfandroidschool.ui.main.create.CreateMemeViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.detail.DetailMemeViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.main.PopularMemesViewModel
import ru.yellowshark.surfandroidschool.ui.main.popular.search.MemeSearchFilterViewModel
import ru.yellowshark.surfandroidschool.ui.main.profile.ProfileViewModel

val viewModelsModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { PopularMemesViewModel(get(), get()) }
    viewModel { MemeSearchFilterViewModel(get()) }
    viewModel { CreateMemeViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { DetailMemeViewModel(get()) }
}