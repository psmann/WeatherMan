package one.mann.weatherman.ui.common.models

/* Created by Psmann. */

internal data class DailyForecast(
    val forecastDate: String = "",
    val minTemp: String = "",
    val maxTemp: String = "",
    val forecastIconId: Int = 0
)