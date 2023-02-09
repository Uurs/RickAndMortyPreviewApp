package ua.bvar.data.di

import androidx.annotation.VisibleForTesting
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import ua.bvar.data.repository.UrlProvider
import javax.inject.Singleton

@VisibleForTesting
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    internal fun provideRetrofit(urlProvider: UrlProvider): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlProvider.getBaseUrl())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }
}