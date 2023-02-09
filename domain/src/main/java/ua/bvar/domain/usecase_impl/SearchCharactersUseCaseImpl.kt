package ua.bvar.domain.usecase_impl

import io.reactivex.rxjava3.core.Single
import ua.bvar.data.repository.CharactersRepository
import ua.bvar.domain.mappers.toDomain
import ua.bvar.domain.model.SearchResult
import ua.bvar.domain.usecase.SearchCharactersUseCase
import javax.inject.Inject

internal class SearchCharactersUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
) : SearchCharactersUseCase {

    override fun execute(query: String?, page: Int): Single<SearchResult> {
        return charactersRepository.searchCharacters(query, page)
            .map { resultDto ->
                SearchResult(
                    list = resultDto.results.map { it.toDomain() },
                    hasNext = resultDto.hasNext
                )
            }
    }


}