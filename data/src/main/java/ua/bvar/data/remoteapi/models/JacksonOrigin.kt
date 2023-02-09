package ua.bvar.data.remoteapi.models


import com.fasterxml.jackson.annotation.JsonProperty

internal data class JacksonOrigin(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("url")
    val url: String
)