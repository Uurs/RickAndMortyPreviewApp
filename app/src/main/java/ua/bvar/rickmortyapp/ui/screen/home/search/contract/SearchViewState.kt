package ua.bvar.rickmortyapp.ui.screen.home.search.contract

import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem

data class SearchViewState(
    val list: List<CharacterItem> = emptyList(),
    val favorites: Set<Int> = emptySet(),
    val query: String? = null,
    val page: Int = 1,
    val internetConnection: Boolean = true,
    val hasNext: Boolean = false,
) {
    fun newSearchResults(query: String?, list: List<CharacterItem>, hasNext: Boolean) =
        copy(
            query = query,
            list = list.map { it.copy(isFavorite = favorites.contains(it.id)) },
            page = 1,
            hasNext = hasNext
        )

    fun newPageLoaded(
        query: String?,
        page: Int,
        newPageList: List<CharacterItem>,
        hasNext: Boolean
    ) =
        copy(
            list = list + newPageList.map { it.copy(isFavorite = favorites.contains(it.id)) },
            query = query,
            page = page,
            hasNext = hasNext
        )

    fun internetConnectionUpdate(connected: Boolean): SearchViewState =
        copy(internetConnection = connected)

    fun favoritesUpdate(ids: Set<Int>): SearchViewState =
        copy(favorites = ids, list = list.map { it.copy(isFavorite = ids.contains(it.id)) })
}