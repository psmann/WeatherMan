package one.mann.interactors.data

import one.mann.domain.logic.*
import one.mann.domain.models.weather.*

/* Created by Psmann. */

/** Update lastChecked for the Weather model */
internal fun Weather.updateLastChecked(): Weather = copy(lastChecked = System.currentTimeMillis())

/** Transform API data and map to domain Weather model */
internal fun mapToDomainWeather(
        city: City,
        currentWeather: CurrentWeather,
        dailyForecasts: List<DailyForecast>,
        hourlyForecasts: List<HourlyForecast>
): Weather {

    val sunriseTime = epochToMinutes(currentWeather.sunrise, city.timezone)
    val sunsetTime = epochToMinutes(currentWeather.sunset, city.timezone)
    val hourlyForecastsWithSunPosition = mutableListOf<HourlyForecast>() // Create a new HourlyForecast list with sunPosition

    hourlyForecasts.map {
        hourlyForecastsWithSunPosition.add(
                HourlyForecast(
                        it.forecastTime,
                        it.temperature,
                        it.forecastIconId,
                        sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(it.forecastTime, city.timezone))
                )
        )
    }

    return Weather(
            city,
            currentWeather,
            dailyForecasts,
            hourlyForecastsWithSunPosition,
            feelsLike(currentWeather.currentTemperature, currentWeather.humidity, currentWeather.windSpeed),
            lengthOfDay(currentWeather.sunrise, currentWeather.sunset),
            System.currentTimeMillis(),
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(System.currentTimeMillis(), city.timezone)),
    )
}