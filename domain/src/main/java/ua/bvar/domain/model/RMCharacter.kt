package ua.bvar.domain.model

data class RMCharacter(
    val id: Int,
    val icon: String?,
    val name: String,
    val species: String?,
    val status: String?,
    val isFavorite: Boolean,
)
