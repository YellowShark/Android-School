package ru.yellowshark.surfandroidschool.app.di

import org.koin.dsl.module.module
import ru.yellowshark.surfandroidschool.data.repository.RepositoryImpl
import ru.yellowshark.surfandroidschool.domain.meme.usecase.*
import ru.yellowshark.surfandroidschool.domain.repository.Repository
import ru.yellowshark.surfandroidschool.domain.user.usecase.*

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
    factory<LoginUserUseCase> { LoginUserUseCaseImpl(get(), get()) }
    factory<LogoutUserUseCase> { LogoutUserUseCaseImpl(get()) }
    factory<SaveUserUseCase> { SaveUserUseCaseImpl(get()) }
}