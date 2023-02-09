package ua.bvar.data.remoteapi.models


import com.fasterxml.jackson.annotation.JsonProperty

internal data class JacksonApiSearchResult(
    @JsonProperty("info")
    val info: JacksonInfo,
    @JsonProperty("results")
    val results: List<JacksonApiCharacter>
)