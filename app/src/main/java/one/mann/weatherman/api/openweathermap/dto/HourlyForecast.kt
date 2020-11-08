package one.mann.weatherman.api.openweathermap.dto

/* Created by Psmann. */

/**
 * Data Transfer Object (model) for OpenWeatherMap HourlyForecast API
 * All parameters are nullable to maintain Kotlin null-safety
 */
internal data class HourlyForecast(val list: List<ListObject>?) {

    data class ListObject(
            val dt: Long?,
            val main: Main?,
            val weather: List<Weather>?
    )

    data class Main(val temp: Float?)

    data class Weather(val id: Int?)
}