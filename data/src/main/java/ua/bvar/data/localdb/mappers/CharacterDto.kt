package ua.bvar.data.localdb

import ua.bvar.data.localdb.entities.CharacterEntity
import ua.bvar.data.models.CharacterDto

internal fun CharacterEntity.toPublicApi(): CharacterDto {
    return CharacterDto(
        id = id,
        image = image,
        name = name,
        species = species,
        status = status,
        isFavorite = isFavorite
    )
}

internal fun CharacterDto.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        image = image,
        name = name,
        species = species,
        status = status,
        isFavorite = isFavorite
    )
}