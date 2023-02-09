package ua.bvar.domain.usecase_impl

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ua.bvar.data.repository.CharactersRepository
import ua.bvar.domain.mappers.toDomain
import ua.bvar.domain.model.RMCharacter
import ua.bvar.domain.usecase.GetFavoriteCharactersUseCase
import javax.inject.Inject

internal class GetFavoriteCharactersUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository
) : GetFavoriteCharactersUseCase {
    override fun execute(): Observable<List<RMCharacter>> {
        return repository.getFavoriteCharacters()
            .map { list -> list.map { it.toDomain() } }
    }
}