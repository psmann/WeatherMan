package one.mann.weatherman.framework.data.database

import one.mann.domain.models.NotificationData
import one.mann.weatherman.framework.data.database.entities.*
import one.mann.weatherman.framework.data.database.entities.relations.CurrentWeatherWithHourlyForecasts
import one.mann.domain.models.weather.City as DomainCity
import one.mann.domain.models.weather.CurrentWeather as DomainCurrentWeather
import one.mann.domain.models.weather.DailyForecast as DailyForecastDommain
import one.mann.domain.models.weather.HourlyForecast as HourlyForecastDomain
import one.mann.domain.models.weather.Weather as DomainWeather

/* Created by Psmann. */

internal fun DomainWeather.mapToDbCity(): City = City(
        city.cityId,
        currentWeather.cityName,
        city.coordinatesLat,
        city.coordinatesLong,
        city.timezone,
        city.timeCreated
)

internal fun DomainWeather.mapToDbCurrentWeather(): CurrentWeather = CurrentWeather(
        currentWeather.weatherId,
        currentWeather.currentTemperature,
        feelsLike,
        currentWeather.pressure,
        currentWeather.humidity,
        currentWeather.description,
        currentWeather.iconId,
        currentWeather.sunrise,
        currentWeather.sunset,
        currentWeather.countryFlag,
        currentWeather.clouds,
        currentWeather.windSpeed,
        currentWeather.windDirection,
        currentWeather.lastUpdated,
        currentWeather.visibility,
        dayLength,
        lastChecked,
        sunPosition,
        city.cityId
)

internal fun DomainWeather.mapToDbDailyForecasts(): List<DailyForecast> {
    val dailyForecastsForDb = mutableListOf<DailyForecast>()
    dailyForecasts.map {
        dailyForecastsForDb.add(
                DailyForecast(
                        0,
                        it.forecastDate,
                        it.minTemp,
                        it.maxTemp,
                        it.forecastIconId,
                        city.cityId
                )
        )
    }
    return dailyForecastsForDb
}

internal fun DomainWeather.mapToDbHourlyForecasts(): List<HourlyForecast> {
    val hourlyForecastsForDb = mutableListOf<HourlyForecast>()
    hourlyForecasts.map {
        hourlyForecastsForDb.add(
                HourlyForecast(
                        0,
                        it.forecastTime,
                        it.temperature,
                        it.forecastIconId,
                        it.sunPosition,
                        city.cityId
                )
        )
    }
    return hourlyForecastsForDb
}

internal fun City.mapToDomainCity(): DomainCity = DomainCity(
        cityId,
        coordinatesLat,
        coordinatesLong,
        timezone,
        timeCreated
)

internal fun City.mapToDomainWeather(
        currentWeather: CurrentWeather,
        dailyForecasts: List<DailyForecast>,
        hourlyForecasts: List<HourlyForecast>
): DomainWeather {
    val dailyForecastsDomain = mutableListOf<DailyForecastDommain>()
    val hourlyForecastsDomain = mutableListOf<HourlyForecastDomain>()

    dailyForecasts.map {
        dailyForecastsDomain.add(
                DailyForecastDommain(
                        it.dailyId,
                        it.date,
                        it.minTemp,
                        it.maxTemp,
                        it.iconId
                )
        )
    }
    hourlyForecasts.map {
        hourlyForecastsDomain.add(
                HourlyForecastDomain(
                        it.hourlyId,
                        it.time,
                        it.temperature,
                        it.iconId,
                        it.sunPosition
                )
        )
    }
    return DomainWeather(
            DomainCity(
                    cityId,
                    coordinatesLat,
                    coordinatesLong,
                    timezone,
                    timeCreated
            ),
            DomainCurrentWeather(
                    currentWeather.weatherId,
                    cityName,
                    currentWeather.currentTemperature,
                    currentWeather.pressure,
                    currentWeather.humidity,
                    currentWeather.description,
                    currentWeather.iconId,
                    currentWeather.sunrise,
                    currentWeather.sunset,
                    currentWeather.countryFlag,
                    currentWeather.clouds,
                    currentWeather.windSpeed,
                    currentWeather.windDirection,
                    currentWeather.lastUpdated,
                    currentWeather.visibility
            ),
            dailyForecastsDomain,
            hourlyForecastsDomain,
            currentWeather.feelsLike,
            currentWeather.dayLength,
            currentWeather.lastChecked,
            currentWeather.sunPosition
    )
}

internal fun CurrentWeatherWithHourlyForecasts.mapToDomain(cityName: String, todayForecast: DailyForecast): NotificationData {
    val forecasts = getSortedForecast()

    return NotificationData(
            cityName,
            currentWeather.description,
            currentWeather.currentTemperature,
            todayForecast.minTemp,
            todayForecast.maxTemp,
            currentWeather.iconId,
            currentWeather.sunPosition,
            currentWeather.humidity,
            forecasts[0].time,
            forecasts[0].iconId,
            forecasts[0].sunPosition,
            forecasts[1].time,
            forecasts[1].iconId,
            forecasts[1].sunPosition,
            forecasts[2].time,
            forecasts[2].iconId,
            forecasts[2].sunPosition,
            forecasts[3].time,
            forecasts[3].iconId,
            forecasts[3].sunPosition,
            forecasts[4].time,
            forecasts[4].iconId,
            forecasts[4].sunPosition,
    )
}