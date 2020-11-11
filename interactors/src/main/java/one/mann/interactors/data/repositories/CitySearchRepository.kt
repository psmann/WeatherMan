package one.mann.interactors.data.repositories

import one.mann.domain.model.CitySearchResult
import one.mann.interactors.data.sources.api.CitySearchDataSource
import javax.inject.Inject

/* Created by Psmann. */

class CitySearchRepository @Inject constructor(private val citySearchDataSource: CitySearchDataSource) {

    suspend fun getCitySearch(query: String): List<CitySearchResult> = citySearchDataSource.getCitySearch(query)
}