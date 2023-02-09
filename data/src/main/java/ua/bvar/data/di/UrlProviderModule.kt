package ua.bvar.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.bvar.data.repository.UrlProvider
import ua.bvar.data.repositoryimpl.UrlProviderImpl

@Module
@InstallIn(SingletonComponent::class)
interface UrlProviderModule {

    @Binds
    fun bindUrlProvider(impl: UrlProviderImpl): UrlProvider
}