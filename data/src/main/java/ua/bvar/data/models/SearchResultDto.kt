package ua.bvar.data.models

data class SearchResultDto(
    val results: List<CharacterDto>,
    val hasNext: Boolean,
)