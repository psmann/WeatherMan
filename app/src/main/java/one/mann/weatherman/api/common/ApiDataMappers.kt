package one.mann.weatherman.api.common

import one.mann.weatherman.api.teleport.dto.Timezone
import one.mann.domain.model.CurrentWeather as DomainCurrentWeather
import one.mann.domain.model.DailyForecast as DomainDailyForecast
import one.mann.domain.model.HourlyForecast as DomainHourlyForecast
import one.mann.weatherman.api.openweathermap.dto.CurrentWeather as ApiCurrentWeather
import one.mann.weatherman.api.openweathermap.dto.DailyForecast as ApiDailyForecast
import one.mann.weatherman.api.openweathermap.dto.HourlyForecast as ApiHourlyForecast

internal fun ApiCurrentWeather.mapToDomain(): DomainCurrentWeather = DomainCurrentWeather(
        name,
        main.temp,
        main.pressure.toInt(),
        main.humidity.toInt(),
        weather[0].main,
        weather[0].id,
        sys.sunrise * 1000,
        sys.sunset * 1000,
        sys.country,
        clouds.all.toInt(),
        wind.speed,
        wind.deg.toInt(),
        dt * 1000,
        visibility.toInt()
)

internal fun ApiDailyForecast.ListObject.mapToDomain(): DomainDailyForecast = DomainDailyForecast(
        dt * 1000,
        temp.min,
        temp.max,
        weather[0].id
)

internal fun ApiHourlyForecast.ListObject.mapToDomain(): DomainHourlyForecast = DomainHourlyForecast(
        dt * 1000,
        main.temp,
        weather[0].id
)

internal fun Timezone.mapToString(): String =
        embedded1.locationNearestCities[0].embedded2.locationNearestCity.embedded3.cityTimezone.ianaName