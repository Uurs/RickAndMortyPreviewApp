package ua.bvar.domain.usecase_impl

import io.reactivex.rxjava3.core.Maybe
import ua.bvar.data.repository.CharactersRepository
import ua.bvar.domain.mappers.toDomain
import ua.bvar.domain.model.RMCharacter
import ua.bvar.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

internal class ToggleFavoriteUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
) : ToggleFavoriteUseCase {

    override fun execute(id: Int): Maybe<RMCharacter> {
        return charactersRepository.getCharacterById(id)
            .flatMap { character ->
                val newCharacter = character.copy(isFavorite = !character.isFavorite)
                charactersRepository.saveOrUpdate(newCharacter)
                    .andThen(Maybe.just(newCharacter.toDomain()))
            }
    }
}