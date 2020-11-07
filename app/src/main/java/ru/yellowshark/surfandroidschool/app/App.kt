package ru.yellowshark.surfandroidschool.app

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.yellowshark.surfandroidschool.app.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(
            applicationContext,
            listOf(viewModelsModule, repositoryModule, networkModule, sessionModule, daoModule)
        )
    }
}