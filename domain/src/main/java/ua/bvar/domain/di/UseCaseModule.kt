package ua.bvar.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.bvar.domain.usecase.GetCharacterDetailsUseCase
import ua.bvar.domain.usecase.GetFavoriteCharactersUseCase
import ua.bvar.domain.usecase.GetInternetConnectionUseCase
import ua.bvar.domain.usecase.SearchCharactersUseCase
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.domain.usecase_impl.GetCharacterDetailsUseCaseImpl
import ua.bvar.domain.usecase_impl.GetFavoriteCharactersUseCaseImpl
import ua.bvar.domain.usecase_impl.GetInternetConnectionUseCaseImpl
import ua.bvar.domain.usecase_impl.SearchCharactersUseCaseImpl
import ua.bvar.domain.usecase_impl.ToggleFavoriteUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface UseCaseModule {
    @Binds
    fun bindSearchCharactersUseCase(impl: SearchCharactersUseCaseImpl): SearchCharactersUseCase

    @Binds
    fun bindToggleFavoriteUseCase(impl: ToggleFavoriteUseCaseImpl): ToggleFavoriteUseCase

    @Binds
    fun bindGetInternetConnectionUseCase(impl: GetInternetConnectionUseCaseImpl): GetInternetConnectionUseCase

    @Binds
    fun bindGetFavoriteCharactersUseCase(impl: GetFavoriteCharactersUseCaseImpl): GetFavoriteCharactersUseCase

    @Binds
    fun bindGetCharacterDetailsUseCase(impl: GetCharacterDetailsUseCaseImpl): GetCharacterDetailsUseCase
}