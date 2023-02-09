package ua.bvar.data.remoteapi.mappers

import ua.bvar.data.models.CharacterDetailsDto
import ua.bvar.data.models.CharacterDto
import ua.bvar.data.remoteapi.models.JacksonCharacterDetailsResult

internal fun JacksonCharacterDetailsResult.toPublicApi(
    isFavorite: Boolean = false
): CharacterDetailsDto =
    CharacterDetailsDto(
        characterDto = CharacterDto(
            id = id,
            image = image,
            name = name,
            species = species,
            status = status,
            isFavorite = isFavorite
        ),
        originName = origin.name,
        gender = gender,
        lastKnownLocationName = location.name,
    )