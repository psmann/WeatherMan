package one.mann.weatherman.api.openweathermap.dto

internal data class HourlyForecast(val list: List<ListObject>) {

    data class ListObject(
            val dt: Long,
            val main: Main,
            val weather: List<Weather>
    )

    data class Main(val temp: Float)

    data class Weather(val id: Int)
}