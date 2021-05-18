package one.mann.interactors.data

import one.mann.domain.logic.*
import one.mann.domain.models.UnitsType
import one.mann.domain.models.UnitsType.*
import one.mann.domain.models.weather.*

/* Created by Psmann. */

/** Transform API data and map to domain Weather model */
internal fun mapToDomainWeather(
    city: City,
    currentWeather: CurrentWeather,
    dailyForecasts: List<DailyForecast>,
    hourlyForecasts: List<HourlyForecast>
): Weather {

    val sunriseTime = epochToMinutes(currentWeather.sunrise, city.timezone)
    val sunsetTime = epochToMinutes(currentWeather.sunset, city.timezone)
    val hourlyForecastsWithSunPosition = mutableListOf<HourlyForecast>() // Create HourlyForecast list with sunPosition

    hourlyForecasts.map {
        hourlyForecastsWithSunPosition.add(
            it.copy(
                sunPosition = sunPositionBias(
                    sunriseTime,
                    sunsetTime,
                    epochToMinutes(it.forecastTime, city.timezone)
                )
            )
        )
    }

    return Weather(
        city,
        currentWeather.copy(countryFlag = countryCodeToEmoji(currentWeather.countryFlag)),
        dailyForecasts,
        hourlyForecastsWithSunPosition,
        feelsLike(currentWeather.currentTemperature, currentWeather.humidity, currentWeather.windSpeed),
        lengthOfDay(currentWeather.sunrise, currentWeather.sunset),
        System.currentTimeMillis(),
        sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(System.currentTimeMillis(), city.timezone)),
    ).applyUnits(currentWeather.units, true)
}

/** Apply units (Imperial or Metric) to weather parameters */
internal fun Weather.applyUnits(units: String, isDataFromApi: Boolean): Weather {
    // Use setUnits() if data is retrieved from the API, otherwise use changeUnits()
    fun Float.set(units: String, type: UnitsType): Float {
        return if (isDataFromApi) setUnits(units, type) else changeUnits(units, type)
    }

    val currentWeatherWithUnits = currentWeather.copy(
        currentTemperature = currentWeather.currentTemperature.set(units, TEMPERATURE),
        windSpeed = currentWeather.windSpeed.set(units, WIND),
        visibility = currentWeather.visibility.set(units, VISIBILITY)
    )
    val dailyForecastsWithUnits = dailyForecasts.map {
        it.copy(
            minTemp = it.minTemp.set(units, TEMPERATURE),
            maxTemp = it.maxTemp.set(units, TEMPERATURE)
        )
    }
    val hourlyForecastsWithUnits = hourlyForecasts.map { it.copy(temperature = it.temperature.set(units, TEMPERATURE)) }

    return copy(
        currentWeather = currentWeatherWithUnits,
        dailyForecasts = dailyForecastsWithUnits,
        hourlyForecasts = hourlyForecastsWithUnits,
        feelsLike = feelsLike.set(units, TEMPERATURE)
    )
}