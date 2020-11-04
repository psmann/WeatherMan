package one.mann.weatherman.api.openweathermap

/** Return file name of vector asset (day) corresponding to the code received from API call */
internal fun dayIcons(code: Int): String = when (code) {
    in 200..232 -> "weather_type_thunder" // Thunderstorm
    in 300..310 -> "weather_type_rainy_2" // Light rain
    in 311..321 -> "weather_type_rainy_3" // Medium rain
    in 500..510, in 512..531 -> "weather_type_rainy_6" // Heavy rain
    511, in 603..619 -> "weather_type_rainy_7" // Freezing rain, sleet
    600, 620 -> "weather_type_snowy_2" // Light snow
    601, 621 -> "weather_type_snowy_3" // Medium snow
    602, 622 -> "weather_type_snowy_6" // Heavy snow
    in 700..781 -> "weather_type_hazy" // Mist, fog, dust etc
    800 -> "weather_type_clear_day" // Clear sky
    801 -> "weather_type_cloudy_day_1" // Cloud cover 11%-25%
    802 -> "weather_type_cloudy_day_2" // Cloud cover 26%-50%
    803 -> "weather_type_cloudy_day_3" // Cloud cover 51%-85%
    804 -> "weather_type_cloudy" // Cloud cover 86%-100%
    else -> "weather_type_cloud_unknown" // Cloud with question mark for unknown code
}

/** Return file name of vector asset (night) corresponding to the code received from API call */
internal fun nightIcons(code: Int): String = when (code) {
    in 200..232 -> "weather_type_thunder" // Thunderstorm
    in 300..310 -> "weather_type_rainy_4" // Light rain
    in 311..321 -> "weather_type_rainy_5" // Medium rain
    in 500..510, in 512..531 -> "weather_type_rainy_6" // Heavy rain
    511, in 603..619 -> "weather_type_rainy_7" // Freezing rain, sleet
    600, 620 -> "weather_type_snowy_4" // Light snow
    601, 621 -> "weather_type_snowy_5" // Medium snow
    602, 622 -> "weather_type_snowy_6" // Heavy snow
    in 700..781 -> "weather_type_hazy" // Mist, fog, dust etc
    800 -> "weather_type_clear_night" // Clear sky
    801 -> "weather_type_cloudy_night_1" // Cloud cover 11%-25%
    802 -> "weather_type_cloudy_night_2" // Cloud cover 26%-50%
    803 -> "weather_type_cloudy_night_3" // Cloud cover 51%-85%
    804 -> "weather_type_cloudy" // Cloud cover 86%-100%
    else -> "weather_type_cloud_unknown" // Cloud with question mark for unknown code
}

/** Check if current weather is overcast */
internal fun isOvercast(code: Int): Boolean = when(code) {
    in 200..232, in 500..510, in 512..531, 511, in 602..619, 622, in 700..781, 804 -> true
    else -> false
}