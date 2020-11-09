package one.mann.weatherman.api.tomtom

import one.mann.domain.model.CitySearchResult
import one.mann.interactors.data.sources.api.CitySearchDataSource

/* Created by Psmann. */

internal class TomTomSearchDataSource: CitySearchDataSource {
    override suspend fun getCitySearch(cityNameQuery: String): CitySearchResult {
        TODO("Not yet implemented")
    }
}