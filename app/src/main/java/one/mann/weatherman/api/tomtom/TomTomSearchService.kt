package one.mann.weatherman.api.tomtom

import one.mann.weatherman.api.tomtom.dto.FuzzySearch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/* Created by Psmann. */

internal interface TomTomSearchService {

    @GET("{query}.json")
    suspend fun getSearch(
        @Path("query") citySearchQuery: String,
        @Query("typeahead") typeahead: Boolean = true,
        @Query("limit") limit: Int = 5,
        @Query("idxSet") idxSet: String = "Geo"
    ): FuzzySearch
}