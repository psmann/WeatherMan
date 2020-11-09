package one.mann.interactors.data.sources.api

import one.mann.domain.model.CitySearchResult

/* Created by Psmann. */

interface CitySearchDataSource {

    suspend fun getCitySearch(cityNameQuery: String): CitySearchResult
}