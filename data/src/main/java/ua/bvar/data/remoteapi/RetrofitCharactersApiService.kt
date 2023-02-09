package ua.bvar.data.remoteapi

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.bvar.data.remoteapi.models.JacksonApiSearchResult
import ua.bvar.data.remoteapi.models.JacksonCharacterDetailsResult

internal interface RetrofitCharactersApiService {

    @GET("character")
    fun searchCharacters(
        @Query("name") query: String?,
        @Query("page") page: Int?
    ): Single<JacksonApiSearchResult>

    @GET("character/{id}")
    fun getCharacterDetails(
        @Path("id") id: Int
    ): Single<JacksonCharacterDetailsResult>
}