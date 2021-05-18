package one.mann.domain.models.weather

/* Created by Psmann. */

data class Weather(
    val city: City = City(),
    val currentWeather: CurrentWeather = CurrentWeather(),
    val dailyForecasts: List<DailyForecast> = listOf(),
    val hourlyForecasts: List<HourlyForecast> = listOf(),
    val feelsLike: Float = 0f,
    val dayLength: String = "",
    val lastChecked: Long = 0,
    val sunPosition: Float = 0f
)