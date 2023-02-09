package ua.bvar.data.remoteapi.mappers

import ua.bvar.data.models.CharacterDto
import ua.bvar.data.remoteapi.models.JacksonApiCharacter

internal fun JacksonApiCharacter.toPublicApi(): CharacterDto {
    return CharacterDto(
        id = id,
        image = image,
        name = name,
        species = species,
        status = status,
        isFavorite = false
    )
}