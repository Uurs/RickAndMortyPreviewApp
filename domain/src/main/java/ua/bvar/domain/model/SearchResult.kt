package ua.bvar.domain.model

data class SearchResult(
    val list: List<RMCharacter>,
    val hasNext: Boolean
)