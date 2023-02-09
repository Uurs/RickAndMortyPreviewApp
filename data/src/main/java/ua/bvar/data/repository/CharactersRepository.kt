package ua.bvar.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ua.bvar.data.models.CharacterDetailsDto
import ua.bvar.data.models.CharacterDto
import ua.bvar.data.models.SearchResultDto

interface CharactersRepository {
    fun searchCharacters(query: String?, page: Int): Single<SearchResultDto>
    fun getFavoriteCharacters(): Observable<List<CharacterDto>>
    fun getCharacterById(id: Int): Maybe<CharacterDto>
    fun saveOrUpdate(newCharacter: CharacterDto): Completable
    fun getCharacterDetails(id: Int): Single<CharacterDetailsDto>
}