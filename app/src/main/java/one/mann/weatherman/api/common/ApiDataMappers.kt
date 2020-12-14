package one.mann.weatherman.api.common

import one.mann.domain.logic.countryCodeToEmoji
import one.mann.domain.models.CitySearchResult
import one.mann.weatherman.api.teleport.dto.Timezone
import one.mann.weatherman.api.tomtom.dto.FuzzySearch
import one.mann.domain.models.weather.CurrentWeather as DomainCurrentWeather
import one.mann.domain.models.weather.DailyForecast as DomainDailyForecast
import one.mann.domain.models.weather.HourlyForecast as DomainHourlyForecast
import one.mann.weatherman.api.openweathermap.dto.CurrentWeather as ApiCurrentWeather
import one.mann.weatherman.api.openweathermap.dto.DailyForecast as ApiDailyForecast
import one.mann.weatherman.api.openweathermap.dto.HourlyForecast as ApiHourlyForecast

/* Created by Psmann. */

/** Map API OWM CurrentWeather to Domain, all parameters are nullable and are given default values */
internal fun ApiCurrentWeather.mapToDomain(): DomainCurrentWeather = DomainCurrentWeather(
        name ?: "Earth",
        main?.temp ?: 0f,
        main?.pressure?.toInt() ?: 0,
        main?.humidity?.toInt() ?: 0,
        weather?.get(0)?.main ?: "",
        weather?.get(0)?.id ?: 0,
        sys?.sunrise?.times(1000) ?: 1000000000000,
        sys?.sunset?.times(1000) ?: 1000000050000,
        sys?.country ?: "AC",
        clouds?.all?.toInt() ?: 0,
        wind?.speed ?: 0f,
        wind?.deg?.toInt() ?: 0,
        dt?.times(1000) ?: 1000000000000,
        visibility ?: 0f
)

/** Map API OWM HourlyForecast to Domain, all parameters are nullable and are given default values */
internal fun ApiHourlyForecast.ListObject?.mapToDomain(): DomainHourlyForecast = DomainHourlyForecast(
        this?.dt?.times(1000) ?: 1000000000000,
        this?.main?.temp ?: 0f,
        this?.weather?.get(0)?.id ?: 0
)

/** Map API OWM DailyForecast to Domain, all parameters are nullable and are given default values */
internal fun ApiDailyForecast.ListObject?.mapToDomain(): DomainDailyForecast = DomainDailyForecast(
        this?.dt?.times(1000) ?: 1000000000000,
        this?.temp?.min ?: 0f,
        this?.temp?.max ?: 0f,
        this?.weather?.get(0)?.id ?: 0
)

/** Map API Teleport Timezone to String to be used in Domain logic, parameter is nullable and is given a default value */
internal fun Timezone?.mapToString(): String {
    return this?.embedded1?.locationNearestCities?.get(0)?.embedded2?.locationNearestCity?.embedded3?.cityTimezone?.ianaName ?: ""
}

/** Map API TomTom Search to Domain, all parameters are nullable and are given default values */
internal fun FuzzySearch.mapToDomain(): List<CitySearchResult> {
    val citySearchResultList = mutableListOf<CitySearchResult>()
    results?.forEach { result ->
        result.position?.let { pos ->
            // Only add address if it has valid lat and lon parameters
            if (pos.lat != null && pos.lon != null) {
                val countryEmoji: String = result.address?.countryCode?.let { countryCodeToEmoji(it) } ?: ""
                val splitName: List<String>? = result.address?.freeformAddress?.split(",") // Split string at ','
                splitName?.let { name ->
                    // Added an If condition since some names do not contain a ','
                    citySearchResultList.add(
                            if (name.size > 1) CitySearchResult(name[0], name[1], pos.lat, pos.lon, countryEmoji)
                            else CitySearchResult(name[0], "", pos.lat, pos.lon, countryEmoji)
                    )
                }
            }
        }
    }
    return citySearchResultList
}