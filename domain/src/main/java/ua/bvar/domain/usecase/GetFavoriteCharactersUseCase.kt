package ua.bvar.domain.usecase

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ua.bvar.domain.model.RMCharacter

interface GetFavoriteCharactersUseCase {
    fun execute(): Observable<List<RMCharacter>>
}