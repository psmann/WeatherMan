package one.mann.weatherman.api.openweathermap.dto

/* Created by Psmann. */

/** Model class for API dailyForecast
 * All parameters are set to nullable to make sure Kotlin null-safety never breaks
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