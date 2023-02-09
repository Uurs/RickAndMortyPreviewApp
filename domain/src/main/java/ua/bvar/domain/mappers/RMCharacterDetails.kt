package ua.bvar.domain.mappers

import ua.bvar.data.models.CharacterDetailsDto
import ua.bvar.domain.model.RMCharacterDetails

fun CharacterDetailsDto.toDomain(): RMCharacterDetails =
    RMCharacterDetails(
        id = characterDto.id,
        imageUrl = characterDto.image,
        name = characterDto.name,
        species = characterDto.species,
        status = characterDto.status,
        isFavorite = characterDto.isFavorite,
        originName = originName,
        gender = gender,
        lastKnownLocationName = lastKnownLocationName,
    )