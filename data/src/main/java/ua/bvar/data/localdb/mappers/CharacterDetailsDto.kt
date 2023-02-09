package ua.bvar.data.localdb.mappers

import ua.bvar.data.localdb.entities.CharacterMetadataEntity
import ua.bvar.data.models.CharacterDetailsDto

internal fun CharacterDetailsDto.toCharactersMetadataEntity() = CharacterMetadataEntity(
    id = characterDto.id,
    originName = originName,
    gender = gender,
    lastKnownLocationName = lastKnownLocationName
)