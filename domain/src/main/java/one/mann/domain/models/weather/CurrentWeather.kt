package one.mann.domain.models.weather

/* Created by Psmann. */

data class CurrentWeather(
    val weatherId: Int = 0,
    val cityName: String = "",
    val currentTemperature: Float = 0f,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val description: String = "",
    val iconId: Int = 0,
    var sunrise: Long = 0,
    var sunset: Long = 0,
    val countryFlag: String = "",
    val clouds: Int = 0,
    val windSpeed: Float = 0f,
    val windDirection: Int = 0,
    var lastUpdated: Long = 0,
    val visibility: Float = 0f,
    val units: String = ""
)