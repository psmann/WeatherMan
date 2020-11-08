package one.mann.weatherman.api.openweathermap.dto

/* Created by Psmann. */

/** Model class for API hourlyForecast
 * All parameters are set to nullable to make sure Kotlin null-safety never breaks
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