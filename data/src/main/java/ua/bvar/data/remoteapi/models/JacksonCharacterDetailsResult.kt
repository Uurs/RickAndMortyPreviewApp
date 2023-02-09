package ua.bvar.data.remoteapi.models


import com.fasterxml.jackson.annotation.JsonProperty

internal data class JacksonCharacterDetailsResult(
    @JsonProperty("created")
    val created: String,
    @JsonProperty("episode")
    val episode: List<String>,
    @JsonProperty("gender")
    val gender: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("image")
    val image: String,
    @JsonProperty("location")
    val location: JacksonLocation,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("origin")
    val origin: JacksonOrigin,
    @JsonProperty("species")
    val species: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("url")
    val url: String
)