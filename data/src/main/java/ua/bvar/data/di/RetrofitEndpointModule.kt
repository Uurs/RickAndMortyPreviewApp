package ua.bvar.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ua.bvar.data.remoteapi.RetrofitCharactersApiService

@Module
@InstallIn(SingletonComponent::class)
internal class RetrofitEndpointModule {

    @Provides
    fun provideRetrofitApiEndpoint(retrofit: Retrofit): RetrofitCharactersApiService {
        return retrofit.create(RetrofitCharactersApiService::class.java)
    }
}