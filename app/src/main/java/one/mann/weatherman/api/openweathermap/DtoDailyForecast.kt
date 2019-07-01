package one.mann.weatherman.api.openweathermap

internal class DtoDailyForecast(var list: List<ListObject>) {

    data class ListObject(
            var dt: Long,
            var temp: Temp,
            var weather: List<Weather>
    )

    data class Temp(
            var min: Float,
            var max: Float
    )

    data class Weather(
            //var main: String,
            var icon: String
    )
}