package one.mann.weatherman.api.common

import one.mann.domain.model.CurrentWeather
import one.mann.domain.model.DailyForecast
import one.mann.domain.model.HourlyForecast
import one.mann.weatherman.api.openweathermap.DtoCurrentWeather
import one.mann.weatherman.api.openweathermap.DtoDailyForecast
import one.mann.weatherman.api.openweathermap.DtoHourlyForecast
import one.mann.weatherman.api.teleport.DtoTimezone

internal fun DtoCurrentWeather.mapToDomain(): CurrentWeather = CurrentWeather(
        name,
        main.temp,
        main.pressure,
        main.humidity,
        weather[0].main,
        weather[0].id,
        sys.sunrise * 1000,
        sys.sunset * 1000,
        sys.country,
        clouds.all,
        wind.speed,
        wind.deg,
        dt * 1000,
        visibility
)

internal fun DtoDailyForecast.ListObject.mapToDomain(): DailyForecast = DailyForecast(
        dt * 1000,
        temp.min,
        temp.max,
        weather[0].id
)

internal fun DtoHourlyForecast.ListObject.mapToDomain(): HourlyForecast = HourlyForecast(
        dt * 1000,
        main.temp,
        weather[0].id
)

internal fun DtoTimezone.mapToString(): String =
        embedded1.locationNearestCities[0].embedded2.locationNearestCity.embedded3.cityTimezone.ianaName