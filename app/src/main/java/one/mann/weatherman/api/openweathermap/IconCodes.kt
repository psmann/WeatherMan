package one.mann.weatherman.api.openweathermap

/** Return file name of vector asset corresponding to the code received from API call */
internal fun dayIcons(code: Int): String = when (code) {
    in 200..232 -> "thunder" // Thunderstorm
    in 300..310 -> "rainy_2" // Light rain
    in 311..321 -> "rainy_3" // Medium rain
    in 500..510, in 512..531 -> "rainy_6" // Heavy rain
    511 -> "rainy_7" // Freezing rain
    600, 620 -> "snowy_2" // Light snow
    601, 621 -> "snowy_3" // Medium snow
    602, 622 -> "snowy_6" // Heavy snow
    in 603..619 -> "rainy_7" // Freezing rain, sleet
    in 700..781 -> "hazy" // Mist, fog, dust etc
    800 -> "day" // Clear sky
    801 -> "cloudy_day_1" // Cloud cover 11-25%
    802 -> "cloudy_day_2" // Cloud cover 26-50%
    803 -> "cloudy_day_3" // Cloud cover 51-85%
    804 -> "cloudy" // loud cover 86-100%
    else -> "cloud_unknown" // Cloud with question mark for unknown code
}

internal fun nightIcons(code: Int): String = when (code) {
    in 200..232 -> "thunder" // Thunderstorm
    in 300..310 -> "rainy_4" // Light rain
    in 311..321 -> "rainy_5" // Medium rain
    in 500..510, in 512..531 -> "rainy_6" // Heavy rain
    511 -> "rainy_7" // Freezing rain
    600, 620 -> "snowy_4" // Light snow
    601, 621 -> "snowy_5" // Medium snow
    602, 622 -> "snowy_6" // Heavy snow
    in 603..619 -> "rainy_7" // Freezing rain, sleet
    in 700..781 -> "hazy" // Mist, fog, dust etc
    800 -> "night" // Clear sky
    801 -> "cloudy_night_1" // Cloud cover 11-25%
    802 -> "cloudy_night_2" // Cloud cover 26-50%
    803 -> "cloudy_night_3" // Cloud cover 51-85%
    804 -> "cloudy" // loud cover 86-100%
    else -> "cloud_unknown" // Cloud with question mark for unknown code
}