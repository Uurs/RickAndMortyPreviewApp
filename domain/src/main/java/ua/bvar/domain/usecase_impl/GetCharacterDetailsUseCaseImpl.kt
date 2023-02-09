package ua.bvar.domain.usecase_impl

import io.reactivex.rxjava3.core.Single
import ua.bvar.data.repository.CharactersRepository
import ua.bvar.domain.mappers.toDomain
import ua.bvar.domain.model.RMCharacterDetails
import ua.bvar.domain.usecase.GetCharacterDetailsUseCase
import javax.inject.Inject

internal class GetCharacterDetailsUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository,
) : GetCharacterDetailsUseCase {

    override fun execute(id: Int): Single<RMCharacterDetails> {
        return repository.getCharacterDetails(id)
            .map { it.toDomain() }
    }
}