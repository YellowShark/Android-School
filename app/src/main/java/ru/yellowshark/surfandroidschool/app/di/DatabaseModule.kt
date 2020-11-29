package ru.yellowshark.surfandroidschool.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import ru.yellowshark.surfandroidschool.data.db.MemesDatabase

val daoModule = module {
    single { provideDatabase(androidContext()) }
    factory { provideMemesDao(get()) }
}

private fun provideDatabase(context: Context) = MemesDatabase(context)

private fun provideMemesDao(db: MemesDatabase) = db.memesDao()