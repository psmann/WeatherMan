package one.mann.weatherman.api.openweathermap

internal class DailyForecastDto(var list: List<List_>?) {

    data class List_(
            var dt: Long = 0,
            var temp: Temp? = null,
            var weather: List<Weather>?
    )

    data class Temp(
            var min: Float,
            var max: Float
    )

    data class Weather(
            var main: String?,
            var icon: String?
    )
}