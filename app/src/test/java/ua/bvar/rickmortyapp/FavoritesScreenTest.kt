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
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.rickmortyapp.core.MockDatabaseRule
import ua.bvar.rickmortyapp.core.RxJavaSchedulersRule
import ua.bvar.rickmortyapp.core.mappers.toCharacterItem
import ua.bvar.rickmortyapp.di.MockDatabaseModule.Companion.DB_SETUP_V1_NO_FAVORITES
import ua.bvar.rickmortyapp.di.MockDatabaseModule.Companion.DB_SETUP_V1_WITH_FAVORITES
import ua.bvar.rickmortyapp.ui.screen.home.favorites.FavoritesViewModel
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class
)
@HiltAndroidTest
class FavoritesScreenTest : BaseScreenTest<FavoritesViewModel>() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()

    @get:Rule
    val mockDatabaseRule = MockDatabaseRule()

    @Inject
    lateinit var getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase

    @Inject
    lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Before
    fun inject() {
        hiltRule.inject()
    }

    override fun createViewModel(): FavoritesViewModel =
        FavoritesViewModel(getFavoriteCharactersUseCase, toggleFavoriteUseCase)

    @Test
    fun `test initial state without favorites`() {
        testEnv(DB_SETUP_V1_NO_FAVORITES) { viewModel ->
            Truth.assertThat(viewModel.state.value).isNotNull()
            Truth.assertThat(viewModel.state.value?.list).isEmpty()
        }
    }

    @Test
    fun `test initial state with favorites`() {
        testEnv(DB_SETUP_V1_WITH_FAVORITES) { viewModel ->
            Truth.assertThat(viewModel.state.value).isNotNull()
            Truth.assertThat(viewModel.state.value?.list?.all { it.isFavorite }).isTrue()
        }
    }

    @Test
    fun `test toggle favorite`() {
        testEnv(DB_SETUP_V1_WITH_FAVORITES) { viewModel ->
            val stateTestObserver = viewModel.state.test()
            stateTestObserver.awaitCount(2)

            val firstFavoriteItem = getFavoriteCharactersUseCase
                .execute()
                .firstOrError()
                .blockingGet()
                .first()
                .toCharacterItem()

            Truth.assertThat(viewModel.state.value?.list).contains(firstFavoriteItem)

            viewModel.toggleFavorite(firstFavoriteItem.id)
            stateTestObserver.awaitCount(3)

            Truth.assertThat(viewModel.state.value?.list).doesNotContain(firstFavoriteItem)

            val useCaseResults = getFavoriteCharactersUseCase
                .execute()
                .firstOrError()
                .blockingGet()

            Truth.assertThat(useCaseResults).doesNotContain(firstFavoriteItem)
        }
    }
}