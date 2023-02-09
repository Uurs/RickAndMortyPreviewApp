package ua.bvar.rickmortyapp.ui.screen.home.favorites

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ua.bvar.domain.usecase.GetFavoriteCharactersUseCase
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.rickmortyapp.core.BaseViewModel
import ua.bvar.rickmortyapp.core.mappers.toCharacterItem
import ua.bvar.rickmortyapp.ui.screen.home.favorites.contract.FavoriteViewEvent
import ua.bvar.rickmortyapp.ui.screen.home.favorites.contract.FavoritesViewState
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : BaseViewModel() {
    val state: BehaviorSubject<FavoritesViewState> =
        BehaviorSubject.createDefault(FavoritesViewState())
    val event: PublishSubject<FavoriteViewEvent> = PublishSubject.create()

    init {
        getFavoritesInternal()
    }

    fun toggleFavorite(id: Int) {
        toggleFavoriteUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d(TAG, "toggleFavoriteInternal: success") },
                {
                    Log.e(TAG, "failed to toggle favorite", it)
                    event.onNext(FavoriteViewEvent.FailedToggleFavorite)
                }
            )
            .addToClearableDisposable()
    }

    private fun getFavoritesInternal() {
        getFavoriteCharactersUseCase.execute()
            .map { list -> list.map { it.toCharacterItem() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state.update { copy(list = it) } },
                {
                    Log.e(TAG, "Failed to fetch favorites characters")
                    event.onNext(FavoriteViewEvent.FailedFetchData)
                }
            )
            .addToClearableDisposable()
    }

    private companion object {
        const val TAG = "FavoritesVM"
    }
}