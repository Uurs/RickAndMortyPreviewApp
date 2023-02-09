package ua.bvar.domain.model

data class RMCharacterDetails(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val species: String,
    val status: String?,
    val isFavorite: Boolean,
    val originName: String,
    val gender: String,
    val lastKnownLocationName: String,
)
