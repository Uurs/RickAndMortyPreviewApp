package ua.bvar.data.models

data class CharacterDto(
    val id: Int,
    val image: String,
    val name: String,
    val species: String,
    val status: String,
    val isFavorite: Boolean
)