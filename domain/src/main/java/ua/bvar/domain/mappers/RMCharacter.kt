package ua.bvar.domain.mappers

import ua.bvar.data.models.CharacterDto
import ua.bvar.domain.model.RMCharacter

internal fun CharacterDto.toDomain(): RMCharacter {
    return RMCharacter(id, image, name, species, status, isFavorite)
}