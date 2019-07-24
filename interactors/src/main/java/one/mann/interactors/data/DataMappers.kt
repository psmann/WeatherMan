package one.mann.interactors.data

import one.mann.domain.logic.*
import one.mann.domain.model.CurrentWeather
import one.mann.domain.model.DailyForecast
import one.mann.domain.model.Location
import one.mann.domain.model.Weather

// Map all API data to domain Weather model
internal fun mapToWeather(
        currentWeather: CurrentWeather,
        dailyForecast: List<DailyForecast>,
        timezone: String,
        location: Location,
        units: String
): Weather = Weather(
        location.id,
        currentWeather.cityName,
        currentWeather.currentTemperature.setUnitsType(units)
                .addUnits(if (units == IMPERIAL) FAHRENHEIT else CELSIUS),
        feelsLike(currentWeather.currentTemperature, currentWeather.humidity, currentWeather.windSpeed).setUnitsType(units)
                .addUnits(if (units == IMPERIAL) FAHRENHEIT else CELSIUS),
        currentWeather.pressure.addUnits(HECTOPASCAL),
        currentWeather.humidity.addUnits(PERCENT),
        currentWeather.description,
        currentWeather.icon,
        epochToTime(currentWeather.sunrise, timezone),
        epochToTime(currentWeather.sunset, timezone),
        countryCodeToEmoji(currentWeather.countryFlag),
        currentWeather.clouds.addUnits(PERCENT),
        currentWeather.windSpeed.addUnits(METERS_PER_SECOND),
        currentWeather.windDirection.addUnits(DEGREES),
        epochToDate(currentWeather.lastUpdated, timezone),
        currentWeather.visibility.addUnits(METERS),
        lengthOfDay(currentWeather.sunrise, currentWeather.sunset),
        epochToDate(System.currentTimeMillis(), timezone),
        sunPositionBias(epochToMinutes(currentWeather.sunrise, timezone), epochToMinutes(currentWeather.sunset, timezone),
                epochToMinutes(System.currentTimeMillis(), timezone)),
        dailyForecast[0].minTemp.setUnitsType(units)
                .addUnits(if (units == IMPERIAL) FAHRENHEIT else CELSIUS),
        dailyForecast[0].maxTemp.setUnitsType(units)
                .addUnits(if (units == IMPERIAL) FAHRENHEIT else CELSIUS),
        epochToDay(dailyForecast[0].forecastDate),
        dailyForecast[0].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[0].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[0].forecastIcon,
        epochToDay(dailyForecast[1].forecastDate),
        dailyForecast[1].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[1].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[1].forecastIcon,
        epochToDay(dailyForecast[2].forecastDate),
        dailyForecast[2].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[2].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[2].forecastIcon,
        epochToDay(dailyForecast[3].forecastDate),
        dailyForecast[3].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[3].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[3].forecastIcon,
        epochToDay(dailyForecast[4].forecastDate),
        dailyForecast[4].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[4].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[4].forecastIcon,
        epochToDay(dailyForecast[5].forecastDate),
        dailyForecast[5].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[5].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[5].forecastIcon,
        epochToDay(dailyForecast[6].forecastDate),
        dailyForecast[6].minTemp.setUnitsType(units).roundOff(),
        dailyForecast[6].maxTemp.setUnitsType(units).roundOff(),
        dailyForecast[6].forecastIcon,
        location.coordinates[0],
        location.coordinates[1],
        location.coordinates.coordinatesInString()
)