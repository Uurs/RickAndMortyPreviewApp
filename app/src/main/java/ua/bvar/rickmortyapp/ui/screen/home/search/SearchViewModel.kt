package ua.bvar.rickmortyapp.ui.screen.home.search

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ua.bvar.domain.usecase.GetFavoriteCharactersUseCase
import ua.bvar.domain.usecase.GetInternetConnectionUseCase
import ua.bvar.domain.usecase.SearchCharactersUseCase
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.rickmortyapp.core.BaseViewModel
import ua.bvar.rickmortyapp.core.mappers.toCharacterItem
import ua.bvar.rickmortyapp.ui.screen.home.search.contract.SearchViewEvent
import ua.bvar.rickmortyapp.ui.screen.home.search.contract.SearchViewState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCharacterUseCase: SearchCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getInternetConnectionUseCase: GetInternetConnectionUseCase,
    private val getFavoritesCharactersUseCase: GetFavoriteCharactersUseCase,
) : BaseViewModel() {

    val state: BehaviorSubject<SearchViewState> = BehaviorSubject.createDefault(SearchViewState())
    val event: PublishSubject<SearchViewEvent> = PublishSubject.create()

    private val userIntents = BehaviorSubject.create<UserIntent>()

    init {
        watchUserIntents()
        watchFavoritesUpdates()
        watchInternetConnectionState()
        search(null)
    }

    fun search(queryInput: String?) {
        userIntents.onNext(UserIntent.Search(queryInput?.trim()))
    }

    fun loadNextPage() {
        val state = state.value
        if (state?.list.isNullOrEmpty() || state?.hasNext == false) {
            return
        }
        val query = state?.query
        val page = state?.page?.inc() ?: 1
        userIntents.onNext(UserIntent.LoadNextPage(query, page))
    }

    fun toggleFavorite(id: Int) {
        userIntents.onNext(UserIntent.ToggleFavorite(id))
    }

    private fun watchInternetConnectionState() {
        getInternetConnectionUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state.update { internetConnectionUpdate(it.isConnected) } },
                { Log.e(TAG, "error while listen internet connection update", it) }
            )
            .addToClearableDisposable()
    }

    private fun watchFavoritesUpdates() {
        getFavoritesCharactersUseCase.execute()
            .map { list -> list.map { it.id }.toSet() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state.update { favoritesUpdate(it) } },
                { Log.e(TAG, "error while listen favorites update", it) }
            )
            .addToClearableDisposable()
    }

    // region UserIntents

    private fun watchUserIntents() {
        userIntents
            .distinctUntilChanged()
            .subscribe { performUserIntent(it) }
            .addToClearableDisposable()
    }

    private fun performUserIntent(userIntent: UserIntent) {
        when (userIntent) {
            is UserIntent.LoadNextPage -> loadNextPageInternal(userIntent)
            is UserIntent.Search -> searchInternal(userIntent)
            is UserIntent.ToggleFavorite -> toggleFavoriteInternal(userIntent)
        }
    }

    private fun searchInternal(intent: UserIntent.Search) {
        searchCharacterUseCase.execute(intent.query, 1)
            .map { result -> result.list.map { it.toCharacterItem() } to result.hasNext }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { (list, hasNext) ->
                    state.update { newSearchResults(intent.query, list, hasNext) }
                },
                {
                    Log.e(TAG, "failed to fetch data", it)
                    event.onNext(SearchViewEvent.FailedToToGetData)
                }
            )
            .addToClearableDisposable()
    }

    private fun toggleFavoriteInternal(userIntent: UserIntent.ToggleFavorite) {
        toggleFavoriteUseCase.execute(userIntent.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d(TAG, "toggleFavoriteInternal: success") },
                { Log.e(TAG, "toggle favorite failed", it) }
            )
            .addToClearableDisposable()
    }

    private fun loadNextPageInternal(intent: UserIntent.LoadNextPage) {
        searchCharacterUseCase.execute(intent.query, intent.page)
            .map { result -> result.list.map { it.toCharacterItem() } to result.hasNext }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { (list, hasNext) ->
                    state.update { newPageLoaded(intent.query, intent.page, list, hasNext) }
                },
                {
                    Log.e(TAG, "failed to load next page", it)
                    event.onNext(SearchViewEvent.FailedToToGetData)
                }
            )
            .addToClearableDisposable()
    }

    private sealed class UserIntent {
        data class Search(val query: String?) : UserIntent()
        data class LoadNextPage(val query: String?, val page: Int) : UserIntent()

        data class ToggleFavorite(
            val id: Int,
            private val timestamp: Long = System.currentTimeMillis()
        ) : UserIntent()
    }

    // endregion

    private companion object {
        private const val TAG = "SearchVM"
    }
}