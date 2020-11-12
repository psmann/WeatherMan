package one.mann.domain.models.weather

/* Created by Psmann. */

data class DailyForecast(
        val forecastDate: Long,
        val minTemp: Float,
        val maxTemp: Float,
        val forecastIconId: Int
)