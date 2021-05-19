package one.mann.interactors.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.mann.domain.models.CitySearchResult
import one.mann.interactors.data.repositories.CitySearchRepository
import javax.inject.Inject

/* Created by Psmann. */

class GetCitySearch @Inject constructor(private val citySearchRepository: CitySearchRepository) {

    suspend fun invoke(query: String): List<CitySearchResult> = withContext(Dispatchers.IO) {
        citySearchRepository.getCitySearch(query)
    }
}