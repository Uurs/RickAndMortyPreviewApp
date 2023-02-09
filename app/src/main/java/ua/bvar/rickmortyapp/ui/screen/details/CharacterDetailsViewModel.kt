package ua.bvar.rickmortyapp.ui.screen.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ua.bvar.domain.usecase.GetCharacterDetailsUseCase
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import ua.bvar.rickmortyapp.NavArgs
import ua.bvar.rickmortyapp.core.BaseViewModel
import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterDetailsViewEvent
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterMetadata
import ua.bvar.rickmortyapp.ui.screen.details.contract.CharacterDetailsViewState
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : BaseViewModel() {
    val state: BehaviorSubject<CharacterDetailsViewState> = BehaviorSubject.createDefault(CharacterDetailsViewState())
    val event: PublishSubject<CharacterDetailsViewEvent> = PublishSubject.create()

    init {
        val id = savedState.get<Int>(NavArgs.CHARACTER_ID)
        Log.i(TAG, "character details: id = $id")
        if (id != null) {
            getCharacterDetails(id)
        } else {
            event.onNext(CharacterDetailsViewEvent.FailedToGetData)
        }
    }

    private fun getCharacterDetails(id: Int) {
        getCharacterDetailsUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    state.update {
                        characterDataLoaded(
                            CharacterItem(it.id, it.imageUrl, it.name, it.species, it.status, it.isFavorite),
                            CharacterMetadata(it.originName, it.gender, it.lastKnownLocationName)
                        )
                    }
                },
                {
                    Log.e(TAG, "getCharacterDetails: failed", it)
                    event.onNext(CharacterDetailsViewEvent.FailedToGetData)
                }
            )
            .addToClearableDisposable()
    }

    fun toggleFavorite() {
        val id = state.value?.character?.id
        if (id == null) {
            Log.e(TAG, "toggleFavorite: id is null")
            return
        }

        toggleFavoriteUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { state.update { favoriteToggleSuccess(it.isFavorite) } },
                { Log.e(TAG, "toggleFavorite: failed", it) }
            )
            .addToClearableDisposable()
    }

    companion object {
        private const val TAG = "CharacterDetailsViewMod"
    }
}