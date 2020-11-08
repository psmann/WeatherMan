package one.mann.domain.model.weather

/* Created by Psmann. */

data class DailyForecast(
        val forecastDate: Long,
        val minTemp: Float,
        val maxTemp: Float,
        val forecastIconId: Int
)