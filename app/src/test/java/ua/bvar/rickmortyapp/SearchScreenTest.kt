package ua.bvar.rickmortyapp

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ua.bvar.domain.usecase.GetFavoriteCharactersUseCase
import ua.bvar.domain.usecase.GetInternetConnectionUseCase
import ua.bvar.domain.usecase.SearchCharactersUseCase
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.rickmortyapp.core.MockDatabaseRule
import ua.bvar.rickmortyapp.core.RxJavaSchedulersRule
import ua.bvar.rickmortyapp.di.MockDatabaseModule
import ua.bvar.rickmortyapp.di.MockInternetObserverModule
import ua.bvar.rickmortyapp.di.MockWebServerModule
import ua.bvar.rickmortyapp.ui.screen.home.search.SearchViewModel
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class
)
@HiltAndroidTest
class SearchScreenTest : BaseScreenTest<SearchViewModel>() {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()

    @get:Rule
    val mockDatabaseRule = MockDatabaseRule()

    @Inject
    lateinit var searchCharacterUseCase: SearchCharactersUseCase

    @Inject
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Inject
    lateinit var getInternetConnectionUseCase: GetInternetConnectionUseCase

    @Inject
    lateinit var getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase

    @Before
    fun inject() {
        hiltRule.inject()
    }

    override fun createViewModel(): SearchViewModel =
        SearchViewModel(
            searchCharacterUseCase,
            toggleFavoriteUseCase,
            getInternetConnectionUseCase,
            getFavoriteCharactersUseCase
        )

    // region initial state
    @Test
    fun `test initial state with internet connection`() {
        MockInternetObserverModule.setupInternetObserver(true)

        testEnv(
            serverResponses = listOf(MockWebServerModule.GET_CHARACTERS_PAGE_1)
        ) { viewModel ->
            viewModel.state.test().awaitCount(2)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth.assertThat(viewModel.state.value?.hasNext).isTrue()
            Truth.assertThat(viewModel.state.value?.page).isEqualTo(1)
            Truth.assertThat(viewModel.state.value?.query).isNull()
            Truth.assertThat(viewModel.state.value?.list).isNotEmpty()
        }
    }

    @Test
    fun `test initial state without internet connection`() {
        MockInternetObserverModule.setupInternetObserver(false)

        testEnv(
            dbFileName = MockDatabaseModule.DB_SETUP_V1_NO_FAVORITES
        ) { viewModel ->
            val eventObservableTest = viewModel.event.test()
            viewModel.state.test().awaitCount(1)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth.assertThat(viewModel.state.value?.hasNext).isFalse()
            Truth.assertThat(viewModel.state.value?.page).isEqualTo(1)
            Truth.assertThat(viewModel.state.value?.query).isNull()
            Truth.assertThat(viewModel.state.value?.list).isNotEmpty()

            eventObservableTest.assertNoValues()
        }
    }

    @Test
    fun `test initial state without internet connection with empty db`() {
        MockInternetObserverModule.setupInternetObserver(false)

        testEnv { viewModel ->
            val eventObservableTest = viewModel.event.test()
            viewModel.state.test().awaitCount(1)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth.assertThat(viewModel.state.value?.hasNext).isFalse()
            Truth.assertThat(viewModel.state.value?.page).isEqualTo(1)
            Truth.assertThat(viewModel.state.value?.query).isNull()
            Truth.assertThat(viewModel.state.value?.list).isEmpty()

            eventObservableTest.assertNoValues()
        }
    }

    // endregion

    // region search
    @Test
    fun `test search with network connection`() {
        MockInternetObserverModule.setupInternetObserver(true)

        testEnv(
            serverResponses = listOf(
                MockWebServerModule.GET_CHARACTERS_PAGE_1,
                MockWebServerModule.GET_CHARACTERS_PAGE_1_NAME_BIRD
            )
        ) { viewModel ->
            val eventObservableTest = viewModel.event.test()
            val stateTest = viewModel.state.test()
            viewModel.state.test().awaitCount(2)

            viewModel.search("bird")
            stateTest.awaitCount(3)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth.assertThat(viewModel.state.value?.hasNext).isFalse()
            Truth.assertThat(viewModel.state.value?.page).isEqualTo(1)
            Truth.assertThat(viewModel.state.value?.list).hasSize(4)
            Truth.assertThat(viewModel.state.value?.query).isEqualTo("bird")

            eventObservableTest.assertNoValues()
        }
    }

    @Test
    fun `test search without network connection with db cache`() {
        MockInternetObserverModule.setupInternetObserver(true)

        testEnv(
            serverResponses = listOf(
                MockWebServerModule.GET_CHARACTERS_PAGE_1,
            )
        ) { viewModel ->
            val eventObservableTest = viewModel.event.test()
            val stateTest = viewModel.state.test()
            stateTest.awaitCount(2)

            MockInternetObserverModule.setupInternetObserver(false)
            viewModel.search("rick")
            stateTest.awaitCount(3)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth.assertThat(viewModel.state.value?.page).isEqualTo(1)
            Truth.assertThat(viewModel.state.value?.list).hasSize(4)
            Truth.assertThat(viewModel.state.value?.query).isEqualTo("rick")
            Truth.assertThat(viewModel.state.value?.hasNext).isFalse()

            eventObservableTest.assertNoValues()
        }
    }

    // endregion

    // region favorite
    @Test
    fun `test toggle favorite`() {
        MockInternetObserverModule.setupInternetObserver(true)
        testEnv(
            serverResponses = listOf(
                MockWebServerModule.GET_CHARACTERS_PAGE_1,
            )
        ) { viewModel ->
            val eventObservableTest = viewModel.event.test()
            val stateTest = viewModel.state.test()
            stateTest.awaitCount(2)

            viewModel.toggleFavorite(1)
            stateTest.awaitCount(3)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth
                .assertThat(
                    viewModel.state.value?.list?.any { it.id == 1 && it.isFavorite }
                )
                .isTrue()
            eventObservableTest.assertNoValues()
        }
    }

    // endregion

    // region load next page
    @Test
    fun `test load next page`() {
        MockInternetObserverModule.setupInternetObserver(true)
        testEnv(
            serverResponses = listOf(
                MockWebServerModule.GET_CHARACTERS_PAGE_1,
                MockWebServerModule.GET_CHARACTERS_PAGE_2,
            )
        ) { viewModel ->
            val eventObservableTest = viewModel.event.test()
            val stateTest = viewModel.state.test()
            stateTest.awaitCount(2)

            viewModel.loadNextPage()
            stateTest.awaitCount(3)

            Truth.assertThat(viewModel.state).isNotNull()
            Truth.assertThat(viewModel.state.value?.hasNext).isTrue()
            Truth.assertThat(viewModel.state.value?.page).isEqualTo(2)
            Truth.assertThat(viewModel.state.value?.list).hasSize(40)
            Truth.assertThat(viewModel.state.value?.query).isNull()

            eventObservableTest.assertNoValues()
        }
    }

    // endregion
}