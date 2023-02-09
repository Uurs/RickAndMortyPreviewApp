package ua.bvar.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.bvar.data.core.AndroidInternetConnectionObserver
import ua.bvar.data.repository.InternetConnectionObserver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InternetObserverModule {

    @Binds
    @Singleton
    internal abstract fun bindInternetConnectionObserver(
        impl: AndroidInternetConnectionObserver
    ): InternetConnectionObserver
}