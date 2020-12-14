package one.mann.domain.models.weather

/* Created by Psmann. */

data class DailyForecast(
        val forecastDate: Long = 0,
        val minTemp: Float = 0f,
        val maxTemp: Float = 0f,
        val forecastIconId: Int = 0
)