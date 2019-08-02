package one.mann.domain.model

data class CurrentWeather(
        val cityName: String,
        val currentTemperature: Float,
        val pressure: Float,
        val humidity: Int,
        val description: String,
        val iconId: Int,
        var sunrise: Long,
        var sunset: Long,
        val countryFlag: String,
        val clouds: Int,
        val windSpeed: Float,
        val windDirection: Float,
        var lastUpdated: Long,
        val visibility: Float
)