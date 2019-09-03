package one.mann.interactors.data

import one.mann.domain.logic.*
import one.mann.domain.model.UnitsType.*
import one.mann.domain.model.location.Location
import one.mann.domain.model.weather.CurrentWeather
import one.mann.domain.model.weather.DailyForecast
import one.mann.domain.model.weather.HourlyForecast
import one.mann.domain.model.weather.Weather

/** Update lastChecked for the Weather model */
internal fun Weather.updateLastChecked(timezone: String): Weather =
        copy(lastChecked = epochToDate(System.currentTimeMillis(), timezone))

/** Transform API data and map to domain Weather model */
internal fun mapToWeather(currentWeather: CurrentWeather, dailyForecast: List<DailyForecast>,
                          hourlyForecast: List<HourlyForecast>, timezone: String, location: Location,
                          units: String): Weather {
    val sunriseTime = epochToMinutes(currentWeather.sunrise, timezone)
    val sunsetTime = epochToMinutes(currentWeather.sunset, timezone)
    return Weather(
            location.id,
            currentWeather.cityName,
            currentWeather.currentTemperature.setUnits(units, TEMPERATURE).addSuffix(DEGREES),
            feelsLike(currentWeather.currentTemperature, currentWeather.humidity, currentWeather.windSpeed)
                    .setUnits(units, TEMPERATURE).addSuffix(DEGREES),
            currentWeather.pressure.addSuffix(HECTOPASCAL),
            currentWeather.humidity.addSuffix(PERCENT),
            currentWeather.description,
            currentWeather.iconId,
            epochToTime(currentWeather.sunrise, timezone),
            epochToTime(currentWeather.sunset, timezone),
            countryCodeToEmoji(currentWeather.countryFlag),
            currentWeather.clouds.addSuffix(PERCENT),
            currentWeather.windSpeed.setUnits(units, WIND).addSuffix(if (units == IMPERIAL) MILES_PER_HOUR else KM_PER_HOUR),
            currentWeather.windDirection.addSuffix(DEGREES),
            epochToDate(currentWeather.lastUpdated, timezone),
            currentWeather.visibility.setUnits(units, VISIBILITY).addSuffix(if (units == IMPERIAL) MILES else KILO_METERS),
            lengthOfDay(currentWeather.sunrise, currentWeather.sunset),
            epochToDate(System.currentTimeMillis(), timezone),
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(System.currentTimeMillis(), timezone)),
            epochToDay(dailyForecast[0].forecastDate, timezone),
            dailyForecast[0].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[0].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[0].forecastIconId,
            epochToDay(dailyForecast[1].forecastDate, timezone),
            dailyForecast[1].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[1].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[1].forecastIconId,
            epochToDay(dailyForecast[2].forecastDate, timezone),
            dailyForecast[2].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[2].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[2].forecastIconId,
            epochToDay(dailyForecast[3].forecastDate, timezone),
            dailyForecast[3].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[3].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[3].forecastIconId,
            epochToDay(dailyForecast[4].forecastDate, timezone),
            dailyForecast[4].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[4].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[4].forecastIconId,
            epochToDay(dailyForecast[5].forecastDate, timezone),
            dailyForecast[5].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[5].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[5].forecastIconId,
            epochToDay(dailyForecast[6].forecastDate, timezone),
            dailyForecast[6].minTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[6].maxTemp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            dailyForecast[6].forecastIconId,
            epochToHour(hourlyForecast[0].forecastTime, timezone),
            hourlyForecast[0].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[0].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[0].forecastTime, timezone)),
            epochToHour(hourlyForecast[1].forecastTime, timezone),
            hourlyForecast[1].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[1].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[1].forecastTime, timezone)),
            epochToHour(hourlyForecast[2].forecastTime, timezone),
            hourlyForecast[2].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[2].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[2].forecastTime, timezone)),
            epochToHour(hourlyForecast[3].forecastTime, timezone),
            hourlyForecast[3].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[3].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[3].forecastTime, timezone)),
            epochToHour(hourlyForecast[4].forecastTime, timezone),
            hourlyForecast[4].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[4].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[4].forecastTime, timezone)),
            epochToHour(hourlyForecast[5].forecastTime, timezone),
            hourlyForecast[5].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[5].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[5].forecastTime, timezone)),
            epochToHour(hourlyForecast[6].forecastTime, timezone),
            hourlyForecast[6].temp.setUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
            hourlyForecast[6].forecastIconId,
            sunPositionBias(sunriseTime, sunsetTime, epochToMinutes(hourlyForecast[6].forecastTime, timezone)),
            location.coordinates[0],
            location.coordinates[1],
            location.coordinates.coordinatesInString()
    )
}

/** Convert units to Imperial-Metric in a not so efficient way */
internal fun Weather.changeUnits(units: String) = copy(
        currentTemp = currentTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).addSuffix(DEGREES),
        feelsLike = feelsLike.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).addSuffix(DEGREES),
        windSpeed = windSpeed.removeUnits(KM_PER_HOUR, MILES_PER_HOUR).toFloat()
                .changeUnits(units, WIND).addSuffix(if (units == IMPERIAL) MILES_PER_HOUR else KM_PER_HOUR),
        visibility = visibility.removeUnits(KILO_METERS, MILES).toFloat()
                .changeUnits(units, VISIBILITY).addSuffix(if (units == IMPERIAL) MILES else KILO_METERS),
        day1MinTemp = day1MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day1MaxTemp = day1MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day2MinTemp = day2MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day2MaxTemp = day2MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day3MinTemp = day3MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day3MaxTemp = day3MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day4MinTemp = day4MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day4MaxTemp = day4MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day5MinTemp = day5MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day5MaxTemp = day5MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day6MinTemp = day6MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day6MaxTemp = day6MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day7MinTemp = day7MinTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        day7MaxTemp = day7MaxTemp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour03Temp = hour03Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour06Temp = hour06Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour09Temp = hour09Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour12Temp = hour12Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour15Temp = hour15Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour18Temp = hour18Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES),
        hour21Temp = hour21Temp.removeUnits(DEGREES).toFloat()
                .changeUnits(units, TEMPERATURE).roundOff().addSuffix(DEGREES)
)