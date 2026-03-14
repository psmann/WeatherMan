package one.mann.weatherman.ui.common.util

import one.mann.domain.logic.*
import one.mann.weatherman.ui.common.models.*
import one.mann.domain.models.CelestialInfo as DomainCelestialInfo
import one.mann.domain.models.NotificationData as DomainNotificationData
import one.mann.domain.models.weather.Weather as DomainWeather

/* Created by Psmann. */

internal fun DomainWeather.mapToUiWeather(): Weather {
    val dailyForecastsUi = dailyForecasts.map {
        DailyForecast(
            epochToDay(it.forecastDate, city.timezone),
            it.minTemp.roundOff().addSuffix(DEGREES),
            it.maxTemp.roundOff().addSuffix(DEGREES),
            it.forecastIconId
        )
    }
    val hourlyForecastsUi = hourlyForecasts.map {
        HourlyForecast(
            epochToHour(it.forecastTime, city.timezone),
            it.temperature.roundOff().addSuffix(DEGREES),
            it.forecastIconId,
            it.sunPosition
        )
    }

    return Weather(
        City(
            city.cityId,
            currentWeather.cityName,
            listOf(city.coordinatesLat, city.coordinatesLong).coordinatesInString()
        ),
        CurrentWeather(
            currentWeather.currentTemperature.addSuffix(DEGREES),
            feelsLike.addSuffix(DEGREES),
            currentWeather.pressure.addSuffix(HECTOPASCAL),
            currentWeather.humidity.addSuffix(PERCENT),
            currentWeather.description,
            currentWeather.iconId,
            epochToTime(currentWeather.sunrise, city.timezone),
            epochToTime(currentWeather.sunset, city.timezone),
            currentWeather.countryFlag,
            currentWeather.clouds.addSuffix(PERCENT),
            currentWeather.windSpeed.addSuffix(if (currentWeather.units == IMPERIAL) MILES_PER_HOUR else KM_PER_HOUR),
            currentWeather.windDirection.addSuffix(DEGREES),
            epochToDate(currentWeather.lastUpdated, city.timezone),
            currentWeather.visibility.addSuffix(if (currentWeather.units == IMPERIAL) MILES else KILO_METERS),
            dayLength,
            epochToDate(lastChecked, city.timezone),
            sunPosition
        ),
        dailyForecastsUi,
        hourlyForecastsUi
    )
}

internal fun DomainNotificationData.mapToUiNotificationData(): NotificationData = NotificationData(
    cityName,
    description,
    currentTemp.roundOff().addSuffix(DEGREES),
    day1MinTemp.roundOff().addSuffix(DEGREES),
    day1MaxTemp.roundOff().addSuffix(DEGREES),
    iconId,
    sunPosition,
    humidity.addSuffix(PERCENT),
    epochToHour(hour03Time, ""),
    hour03IconId,
    hour03SunPosition,
    epochToHour(hour06Time, ""),
    hour06IconId,
    hour06SunPosition,
    epochToHour(hour09Time, ""),
    hour09IconId,
    hour09SunPosition,
    epochToHour(hour12Time, ""),
    hour12IconId,
    hour12SunPosition,
    epochToHour(hour15Time, ""),
    hour15IconId,
    hour15SunPosition
)

internal fun DomainCelestialInfo.mapToUiCelestialInfo(): CelestialInfo = CelestialInfo(
    // Formatted display strings for Sun info card
    sunAltitude = "%.2f°".format(sunAltDeg),
    sunAzimuth = "%.2f° %s".format(sunAzDeg, azimuthDirection(sunAzDeg)),
    sunRiseInfo = "${formatHours(sunRise)}  (%.2f°, %.2f° %s)".format(
        sunRiseAlt,
        sunRiseAz,
        azimuthDirection(sunRiseAz)
    ),
    sunSetInfo = "${formatHours(sunSet)}  (%.2f°, %.2f° %s)".format(sunSetAlt, sunSetAz, azimuthDirection(sunSetAz)),
    sunZenithInfo = "${formatHours(sunMeridian)}  (%.2f°, %.2f° %s)".format(
        sunPeakAlt,
        sunMeridianAz,
        azimuthDirection(sunMeridianAz)
    ),
    sunNadirInfo = "${formatHours(sunNadirHour)}  (%.2f°, %.2f° %s)".format(
        sunNadirAlt,
        sunNadirAz,
        azimuthDirection(sunNadirAz)
    ),
    // Formatted display strings for Moon info card
    moonAltitude = "%.2f°".format(moonAltDeg),
    moonAzimuth = "%.2f° %s".format(moonAzDeg, azimuthDirection(moonAzDeg)),
    moonRiseInfo = "${formatHours(moonRise)}  (%.2f°, %.2f° %s)".format(
        moonRiseAlt,
        moonRiseAz,
        azimuthDirection(moonRiseAz)
    ),
    moonSetInfo = "${formatHours(moonSet)}  (%.2f°, %.2f° %s)".format(
        moonSetAlt,
        moonSetAz,
        azimuthDirection(moonSetAz)
    ),
    moonZenithInfo = "${formatHours(moonMeridian)}  (%.2f°, %.2f° %s)".format(
        moonPeakAlt,
        moonMeridianAz,
        azimuthDirection(moonMeridianAz)
    ),
    moonNadirInfo = "${formatHours(moonNadirHour)}  (%.2f°, %.2f° %s)".format(
        moonNadirAlt,
        moonNadirAz,
        azimuthDirection(moonNadirAz)
    ),
    moonPhase = moonPhaseName(moonAge),
    moonAge = "%.1f days".format(moonAge),
    moonIllumination = "%.1f%%".format(moonIllumination * 100),
    // Pass raw values for chart rendering
    sunAltDeg = sunAltDeg,
    sunAzDeg = sunAzDeg,
    moonAltDeg = moonAltDeg,
    moonAzDeg = moonAzDeg,
    sunRiseHour = sunRise,
    sunSetHour = sunSet,
    sunRiseAz = sunRiseAz,
    sunSetAz = sunSetAz,
    moonRiseHour = moonRise,
    moonSetHour = moonSet,
    moonRiseAz = moonRiseAz,
    moonSetAz = moonSetAz,
    moonAgeDays = moonAge,
    moonIlluminationFraction = moonIllumination,
    currentHourFraction = currentHourFraction,
    sunMeridianHour = sunMeridian,
    sunPeakAlt = sunPeakAlt,
    sunNadirAlt = sunNadirAlt,
    moonMeridianHour = moonMeridian,
    moonPeakAlt = moonPeakAlt,
    moonNadirAlt = moonNadirAlt,
    isDaytime = isDaytime,
)
