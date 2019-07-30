package one.mann.weatherman.api.openweathermap

internal data class DtoCurrentWeather(
        val main: Main,
        val sys: Sys,
        val wind: Wind,
        val clouds: Clouds,
        val weather: List<Weather>,
        val name: String,
        val dt: Long,
        val visibility: Float
) {
    data class Clouds(val all: Float)

    data class Main(
            val temp: Float,
            val pressure: Float,
            val humidity: Float
    )

    data class Sys(
            val sunrise: Long,
            val sunset: Long,
            val country: String
    )

    data class Weather(
            val main: String,
            val id: Int
    )

    data class Wind(
            val speed: Float,
            val deg: Float
    )
}