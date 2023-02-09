package ua.bvar.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.bvar.data.repository.CharactersRepository
import ua.bvar.data.repositoryimpl.CharactersRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindCharactersRepository(impl: CharactersRepositoryImpl): CharactersRepository
}