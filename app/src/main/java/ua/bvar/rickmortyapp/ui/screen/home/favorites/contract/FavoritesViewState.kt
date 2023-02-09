package ua.bvar.rickmortyapp.ui.screen.home.favorites.contract

import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem

data class FavoritesViewState(
    val list: List<CharacterItem> = emptyList()
)