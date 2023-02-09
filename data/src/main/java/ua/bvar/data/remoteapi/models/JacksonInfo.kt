package ua.bvar.data.remoteapi.models


import com.fasterxml.jackson.annotation.JsonProperty

internal data class JacksonInfo internal constructor(
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("next")
    val next: String?,
    @JsonProperty("pages")
    val pages: Int,
    @JsonProperty("prev")
    val prev: String?
)