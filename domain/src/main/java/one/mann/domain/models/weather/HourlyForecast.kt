package one.mann.domain.models.weather

/* Created by Psmann. */

class HourlyForecast(
        val forecastTime: Long = 0,
        val temperature: Float = 0f,
        val forecastIconId: Int = 0,
        val sunPosition: Float = 0f
)