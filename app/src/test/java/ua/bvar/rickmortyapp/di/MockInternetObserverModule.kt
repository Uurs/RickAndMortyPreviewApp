package ua.bvar.rickmortyapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import ua.bvar.data.di.InternetObserverModule
import ua.bvar.data.repository.InternetConnectionObserver

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [InternetObserverModule::class]
)
class MockInternetObserverModule {

    @Provides
    fun provideInternetConnectionObserver(): InternetConnectionObserver {
        return mockInternetObserverInstance
    }

    companion object {
        val mockInternetObserverInstance = mockk<InternetConnectionObserver>()

        fun setupInternetObserver(internetAvailable: Boolean) {
            every { mockInternetObserverInstance.getNetworkState() } answers { internetAvailable }
            every { mockInternetObserverInstance.observeNetworkState() } answers {
                Observable.just(
                    internetAvailable
                )
            }
        }
    }
}