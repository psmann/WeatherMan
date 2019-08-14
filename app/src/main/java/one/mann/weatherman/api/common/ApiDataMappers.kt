package one.mann.weatherman.api.common

import one.mann.weatherman.api.teleport.dto.Timezone
import one.mann.domain.model.CurrentWeather as DomainCurrentWeather
import one.mann.domain.model.DailyForecast as DomainDailyForecast
import one.mann.domain.model.HourlyForecast as DomainHourlyForecast
import one.mann.weatherman.api.openweathermap.dto.CurrentWeather as ApiCurrentWeather
import one.mann.weatherman.api.openweathermap.dto.DailyForecast as ApiDailyForecast
import one.mann.weatherman.api.openweathermap.dto.HourlyForecast as ApiHourlyForecast

/** Map API currentWeather to Domain, all parameters are nullable and are given default values */
internal fun ApiCurrentWeather.mapToDomain(): DomainCurrentWeather = DomainCurrentWeather(
        name ?: "",
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
        visibility?.toInt() ?: 0
)

/** Map API hourlyForecast to Domain, all parameters are nullable and are given default values */
internal fun ApiHourlyForecast.ListObject?.mapToDomain(): DomainHourlyForecast = DomainHourlyForecast(
        this?.dt?.times(1000) ?: 1000000000000,
        this?.main?.temp ?: 0f,
        this?.weather?.get(0)?.id ?: 0
)

/** Map API dailyForecast to Domain, all parameters are nullable and are given default values */
internal fun ApiDailyForecast.ListObject?.mapToDomain(): DomainDailyForecast = DomainDailyForecast(
        this?.dt?.times(1000) ?: 1000000000000,
        this?.temp?.min ?: 0f,
        this?.temp?.max ?: 0f,
        this?.weather?.get(0)?.id ?: 0
)

internal fun Timezone?.mapToString(): String =
        this?.embedded1?.locationNearestCities?.get(0)?.embedded2?.locationNearestCity?.embedded3?.cityTimezone?.ianaName ?: ""