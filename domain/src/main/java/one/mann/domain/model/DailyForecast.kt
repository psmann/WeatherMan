package one.mann.domain.model

data class DailyForecast(
        var forecastDate: Long,
        var minTemp: Float,
        var maxTemp: Float,
        var forecastIcon: String
)