package one.mann.weatherman.api.tomtom

import one.mann.domain.models.CitySearchResult
import one.mann.interactors.data.sources.api.CitySearchDataSource
import one.mann.weatherman.api.common.mapToDomain
import javax.inject.Inject

/* Created by Psmann. */

internal class TomTomSearchDataSource @Inject constructor(private val tomTomSearchService: TomTomSearchService)
    : CitySearchDataSource {

    override suspend fun getCitySearch(cityNameQuery: String): List<CitySearchResult> {
        return tomTomSearchService.getSearch(cityNameQuery).mapToDomain()
    }
}