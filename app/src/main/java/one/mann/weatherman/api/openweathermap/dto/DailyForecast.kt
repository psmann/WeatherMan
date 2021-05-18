package one.mann.weatherman.api.openweathermap.dto

/* Created by Psmann. */

/**
 * Data Transfer Object (model) for OpenWeatherMap DailyForecast API
 * All parameters are nullable to maintain Kotlin null-safety
 */
internal data class DailyForecast(val list: List<ListObject>?) {

    data class ListObject(
        val dt: Long?,
        val temp: Temp?,
        val weather: List<Weather>?
    )

    data class Temp(
        val min: Float?,
        val max: Float?
    )

    data class Weather(val id: Int?)
}