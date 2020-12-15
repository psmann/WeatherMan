package one.mann.weatherman.ui.common.models

/* Created by Psmann. */

internal data class CurrentWeather(
        val currentTemperature: String = "",
        val feelsLike: String = "",
        val pressure: String = "",
        val humidity: String = "",
        val description: String = "",
        val iconId: Int = 0,
        var sunrise: String = "",
        var sunset: String = "",
        val countryFlag: String = "",
        val clouds: String = "",
        val windSpeed: String = "",
        val windDirection: String = "",
        var lastUpdated: String = "",
        val visibility: String = "",
        val dayLength: String = "",
        val lastChecked: String = "",
        val sunPosition: Float = 0f
)