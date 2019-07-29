package one.mann.weatherman.api.openweathermap

internal class DtoDailyForecast(val list: List<ListObject>) {

    data class ListObject(
            val dt: Long,
            val temp: Temp,
            val weather: List<Weather>
    )

    data class Temp(
            val min: Float,
            val max: Float
    )

    data class Weather(val id: Int)
}