package one.mann.domain.model.weather

data class DailyForecast(
        val forecastDate: Long,
        val minTemp: Float,
        val maxTemp: Float,
        val forecastIconId: Int
)