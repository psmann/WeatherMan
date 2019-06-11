package one.mann.weatherman.api

import one.mann.domain.DailyForecast
import one.mann.domain.Weather
import one.mann.weatherman.api.openweathermap.DtoDailyForecast
import one.mann.weatherman.api.openweathermap.DtoWeather

internal fun DtoWeather.mapToDomain(): Weather = Weather(
        main.temp,
        main.pressure,
        main.humidity,
        weather[0].main,
        weather[0].icon,
        sys.sunrise,
        sys.sunset,
        sys.country,
        clouds.all,
        wind.speed,
        wind.deg,
        name,
        dt,
        visibility
)

internal fun DtoDailyForecast.List_.mapToDomain(): DailyForecast = DailyForecast(
        dt,
        temp.min,
        temp.max,
        weather[0].main,
        weather[0].icon
)