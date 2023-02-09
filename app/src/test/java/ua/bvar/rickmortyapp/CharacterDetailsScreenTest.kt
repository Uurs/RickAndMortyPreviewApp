package ua.bvar.rickmortyapp

import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ua.bvar.domain.usecase.GetCharacterDetailsUseCase
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.rickmortyapp.core.MockDatabaseRule
import ua.bvar.rickmortyapp.core.RxJavaSchedulersRule
import ua.bvar.rickmortyapp.di.MockDatabaseModule
import ua.bvar.rickmortyapp.di.MockInternetObserverModule
import ua.bvar.rickmortyapp.di.MockWebServerModule
import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem
import ua.bvar.rickmortyapp.ui.screen.details.CharacterDetailsViewModel
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterDetailsViewEvent
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterDetailsViewState
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterMetadata
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class
)
@HiltAndroidTest
class CharacterDetailsScreenTest : BaseScreenTest<CharacterDetailsViewModel>() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()

    @get:Rule
    val mockDatabaseRule = MockDatabaseRule()

    @Inject
    lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase

    @Inject
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Before
    fun inject() {
        hiltRule.inject()
    }

    override fun createViewModel(): CharacterDetailsViewModel =
        CharacterDetailsViewModel(
            savedState = SavedStateHandle(mapOf(NavArgs.CHARACTER_ID to 1)),
            getCharacterDetailsUseCase = getCharacterDetailsUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
        )

    @Test
    fun `test initial state`() {
        MockInternetObserverModule.setupInternetObserver(true)
        testEnv(
            serverResponses = listOf(
                MockWebServerModule.GET_CHARACTER_DETAILS_ID_1
            ),
        ) { viewModel ->
            val stateTestObserver = viewModel.state.test()
            val eventObserver = viewModel.event.test()
            stateTestObserver.awaitCount(2)

            Truth.assertThat(viewModel.state.value)
                .isEqualTo(
                    CharacterDetailsViewState(
                        character = CharacterItem(
                            id = 1,
                            name = "Rick Sanchez",
                            icon = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                            status = "Alive",
                            species = "Human",
                            isFavorite = false
                        ),
                        meta = CharacterMetadata(
                            originName = "Earth (C-137)",
                            lastKnownLocationName = "Citadel of Ricks",
                            gender = "Male",
                        )
                    ),
                )
            eventObserver.assertNoValues()
        }
    }

    @Test
    fun `test initial state with failed to load data`() {
        MockInternetObserverModule.setupInternetObserver(true)
        MockWebServerModule.mockWebServer.enqueue(MockResponse().setResponseCode(404))

        testEnv { viewModel ->
            val stateTestObserver = viewModel.state.test()
            val eventObserver = viewModel.event.test()
            stateTestObserver.awaitCount(2)

            Truth.assertThat(viewModel.state.value)
                .isEqualTo(CharacterDetailsViewState())
            eventObserver.assertValue(CharacterDetailsViewEvent.FailedToGetData)
        }
    }

    @Test
    fun `test initial state without internet connection and local cache`() {
        MockInternetObserverModule.setupInternetObserver(false)

        testEnv { viewModel ->
            val stateTestObserver = viewModel.state.test()
            stateTestObserver.awaitCount(1)

            Truth.assertThat(viewModel.state.value)
                .isEqualTo(CharacterDetailsViewState())
        }
    }

    @Test
    fun `test initial state without internet connection and partial local cache`() {
        MockInternetObserverModule.setupInternetObserver(false)

        testEnv(
            dbFileName = MockDatabaseModule.DB_SETUP_V1_NO_FAVORITES
        ) { viewModel ->
            val stateTestObserver = viewModel.state.test()
            stateTestObserver.awaitCount(1)

            Truth.assertThat(viewModel.state.value)
                .isEqualTo(CharacterDetailsViewState())
        }
    }

    @Test
    fun `test toggle favorite`() {
        MockInternetObserverModule.setupInternetObserver(true)

        testEnv(
            serverResponses = listOf(
                MockWebServerModule.GET_CHARACTER_DETAILS_ID_1
            ),
        ) { viewModel ->
            val stateTestObserver = viewModel.state.test()
            val eventObserver = viewModel.event.test()
            stateTestObserver.awaitCount(2)

            viewModel.toggleFavorite()
            stateTestObserver.awaitCount(3)

            Truth
                .assertThat(viewModel.state.value?.character?.isFavorite)
                .isTrue()
            eventObserver.assertNoValues()
        }
    }
}