package ua.bvar.data.repositoryimpl

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ua.bvar.data.Constants
import ua.bvar.data.localdb.dao.CharactersDao
import ua.bvar.data.localdb.mappers.toCharactersMetadataEntity
import ua.bvar.data.localdb.toEntity
import ua.bvar.data.localdb.toPublicApi
import ua.bvar.data.models.CharacterDetailsDto
import ua.bvar.data.models.CharacterDto
import ua.bvar.data.models.SearchResultDto
import ua.bvar.data.remoteapi.RetrofitCharactersApiService
import ua.bvar.data.remoteapi.mappers.toPublicApi
import ua.bvar.data.repository.CharactersRepository
import ua.bvar.data.repository.InternetConnectionObserver
import javax.inject.Inject

internal class CharactersRepositoryImpl @Inject constructor(
    private val internetConnectionObserver: InternetConnectionObserver,
    private val charactersDao: CharactersDao,
    private val charactersApiService: RetrofitCharactersApiService
) : CharactersRepository {

    override fun getFavoriteCharacters(): Observable<List<CharacterDto>> {
        return charactersDao.getFavorites()
            .map { list -> list.map { it.toPublicApi() } }
    }

    override fun getCharacterById(id: Int): Maybe<CharacterDto> {
        return charactersDao.getCharacterById(id)
            .map { it.toPublicApi() }
    }

    override fun saveOrUpdate(newCharacter: CharacterDto): Completable {
        return charactersDao.saveOrUpdate(newCharacter.toEntity())
    }

    override fun getCharacterDetails(id: Int): Single<CharacterDetailsDto> {
        return if (internetConnectionObserver.getNetworkState()) {
            Single.zip(
                charactersApiService.getCharacterDetails(id),
                charactersDao.getFavorites().firstOrError(),
            ) { characterDetails, favorites ->
                val isFavorite = favorites.any { it.id == characterDetails.id }
                characterDetails.toPublicApi(isFavorite)
            }
                .flatMap { characterDetails ->
                    charactersDao
                        .saveOrUpdate(characterDetails.characterDto.toEntity())
                        .andThen(
                            charactersDao.saveOrUpdate(
                                characterDetails.toCharactersMetadataEntity()
                            )
                        )
                        .andThen(Single.just(characterDetails))
                }
        } else {
            Maybe.zip(
                charactersDao.getCharacterById(id),
                charactersDao.getCharacterMetaById(id),
            ) { character, characterMeta ->
                CharacterDetailsDto(
                    characterDto = character.toPublicApi(),
                    originName = characterMeta.originName,
                    gender = characterMeta.gender,
                    lastKnownLocationName = characterMeta.lastKnownLocationName,
                )
            }
                .toSingle()
        }
    }

    override fun searchCharacters(query: String?, page: Int): Single<SearchResultDto> {
        return if (internetConnectionObserver.getNetworkState()) {
            loadFromRemote(query, page)
        } else {
            loadFromLocal(query, page)
        }
    }

    private fun loadFromRemote(query: String?, page: Int): Single<SearchResultDto> =
        Single.zip(
            charactersDao.getFavorites().firstOrError(),
            charactersApiService.searchCharacters(query, page)
        ) { favorites, searchResult ->
            val favSet = favorites.map { it.id }.toSet()
            SearchResultDto(
                results = searchResult
                    .results
                    .map { it.toPublicApi().copy(isFavorite = favSet.contains(it.id)) },
                hasNext = searchResult.info.next != null
            )
        }
            .flatMap { searchResultDto ->
                charactersDao.saveOrUpdate(searchResultDto.results.map { it.toEntity() })
                    .andThen(Single.just(searchResultDto))
            }

    private fun loadFromLocal(query: String?, page: Int): Single<SearchResultDto> {
        val localDbPage = page - 1
        return Single.zip(
            charactersDao.getCharacters(
                query,
                localDbPage * Constants.DEFAULT_ITEM_PER_PAGE,
                Constants.DEFAULT_ITEM_PER_PAGE
            ),
            charactersDao.getCharactersCount(query)
        ) { items, count ->
            val hasNext = items.isNotEmpty() &&
                    (localDbPage * Constants.DEFAULT_ITEM_PER_PAGE + items.size) < count
            SearchResultDto(
                results = items.map { it.toPublicApi() },
                hasNext = hasNext
            )
        }
    }
}