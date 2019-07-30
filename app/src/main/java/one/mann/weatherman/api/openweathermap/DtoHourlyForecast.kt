package one.mann.weatherman.api.openweathermap

internal data class DtoHourlyForecast(val list: List<ListObject>) {

    data class ListObject(
            val dt: Long,
            val main: Main,
            val weather: List<Weather>
    )

    data class Main(val temp: Float)

    data class Weather(val id: Int)
}