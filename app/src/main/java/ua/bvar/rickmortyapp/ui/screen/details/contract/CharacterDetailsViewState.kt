package ua.bvar.rickmortyapp.ui.screen.details.contract

import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem

data class CharacterDetailsViewState(
    val character: CharacterItem? = null,
    val meta: CharacterMetadata? = null,
) {

    fun characterDataLoaded(character: CharacterItem, meta: CharacterMetadata): CharacterDetailsViewState =
        copy(character = character, meta = meta)

    fun favoriteToggleSuccess(isFavorite: Boolean): CharacterDetailsViewState =
        copy(character = character?.copy(isFavorite = isFavorite))
}
