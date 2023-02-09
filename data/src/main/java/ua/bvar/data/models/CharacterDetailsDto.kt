package ua.bvar.data.models

data class CharacterDetailsDto(
    val characterDto: CharacterDto,
    val originName: String,
    val gender: String,
    val lastKnownLocationName: String,
)
