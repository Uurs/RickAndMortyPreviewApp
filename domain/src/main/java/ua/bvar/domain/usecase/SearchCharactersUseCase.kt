package ua.bvar.domain.usecase

import io.reactivex.rxjava3.core.Single
import ua.bvar.domain.model.SearchResult

interface SearchCharactersUseCase {
    fun execute(query: String?, page: Int): Single<SearchResult>
}