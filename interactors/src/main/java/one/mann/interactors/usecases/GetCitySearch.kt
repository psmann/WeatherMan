package one.mann.interactors.usecases

import one.mann.domain.model.CitySearchResult
import one.mann.interactors.data.repositories.CitySearchRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetCitySearch @Inject constructor(private val citySearchRepository: CitySearchRepository) {

    suspend fun invoke(query: String): List<CitySearchResult> = citySearchRepository.getCitySearch(query)
}