package ua.bvar.rickmortyapp.core.mappers

import ua.bvar.domain.model.RMCharacter
import ua.bvar.rickmortyapp.ui.screen.common.model.CharacterItem

fun RMCharacter.toCharacterItem(): CharacterItem {
    return CharacterItem(
        id = id,
        icon = icon,
        name = name,
        species = species,
        status = status,
        isFavorite = isFavorite
    )
}