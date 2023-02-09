package ua.bvar.rickmortyapp.ui.screen.common.model

data class CharacterItem(
    val id: Int,
    val icon: String?,
    val name: String,
    val species: String?,
    val status: String?,
    val isFavorite: Boolean,
)
