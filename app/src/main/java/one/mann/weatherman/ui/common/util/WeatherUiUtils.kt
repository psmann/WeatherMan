package one.mann.weatherman.ui.common.util

import one.mann.weatherman.R

/* Created by Psmann. */

/** Sets layout background depending upon time of day and weather conditions */
internal fun getGradient(sunPosition: Float = 0.1f, isOvercast: Boolean = false): Int = when (sunPosition) {
    in -0.035..0.055, in 0.945..1.035 -> // Dawn-Sunrise and Sunset-Twilight
        if (isOvercast) R.drawable.background_gradient_sunrise_clouds
        else R.drawable.background_gradient_sunrise_clear
    in 0.055..0.945 -> // Day
        if (isOvercast) R.drawable.background_gradient_day_clouds
        else R.drawable.background_gradient_day_clear
    else -> // Night
        if (isOvercast) R.drawable.background_gradient_night_clouds
        else R.drawable.background_gradient_night_clear
}

/** Gets weather icon vector resource ID */
internal fun getUri(code: Int, sunPosition: Float): Int {
    return if (sunPosition in 0.0..1.0) dayIcons(code) else nightIcons(code)
}

/** Checks if current weather is overcast */
internal fun isOvercast(code: Int): Boolean = when (code) {
    in 200..232, in 500..510, in 512..531, 511, in 602..619, 622, in 700..781, 804 -> true
    else -> false
}

/** Returns resource ID of vector asset (day) corresponding to the code received from API call */
internal fun dayIcons(code: Int): Int = when (code) {
    in 200..232 -> R.drawable.weather_type_thunder // Thunderstorm
    in 300..310 -> R.drawable.weather_type_rainy_2 // Light rain
    in 311..321 -> R.drawable.weather_type_rainy_3 // Medium rain
    in 500..510, in 512..531 -> R.drawable.weather_type_rainy_6 // Heavy rain
    511, in 603..619 -> R.drawable.weather_type_rainy_7 // Freezing rain, sleet
    600, 620 -> R.drawable.weather_type_snowy_2 // Light snow
    601, 621 -> R.drawable.weather_type_snowy_3 // Medium snow
    602, 622 -> R.drawable.weather_type_snowy_6 // Heavy snow
    in 700..781 -> R.drawable.weather_type_hazy // Mist, fog, dust etc
    800 -> R.drawable.weather_type_clear_day // Clear sky
    801 -> R.drawable.weather_type_cloudy_day_1 // Cloud cover 11%-25%
    802 -> R.drawable.weather_type_cloudy_day_2 // Cloud cover 26%-50%
    803 -> R.drawable.weather_type_cloudy_day_3 // Cloud cover 51%-85%
    804 -> R.drawable.weather_type_cloudy // Cloud cover 86%-100%
    else -> R.drawable.weather_type_cloud_unknown // Cloud with question mark for unknown code
}

/** Returns resource ID of vector asset (night) corresponding to the code received from API call */
internal fun nightIcons(code: Int): Int = when (code) {
    in 200..232 -> R.drawable.weather_type_thunder // Thunderstorm
    in 300..310 -> R.drawable.weather_type_rainy_4 // Light rain
    in 311..321 -> R.drawable.weather_type_rainy_5 // Medium rain
    in 500..510, in 512..531 -> R.drawable.weather_type_rainy_6 // Heavy rain
    511, in 603..619 -> R.drawable.weather_type_rainy_7 // Freezing rain, sleet
    600, 620 -> R.drawable.weather_type_snowy_4 // Light snow
    601, 621 -> R.drawable.weather_type_snowy_5 // Medium snow
    602, 622 -> R.drawable.weather_type_snowy_6 // Heavy snow
    in 700..781 -> R.drawable.weather_type_hazy // Mist, fog, dust etc
    800 -> R.drawable.weather_type_clear_night // Clear sky
    801 -> R.drawable.weather_type_cloudy_night_1 // Cloud cover 11%-25%
    802 -> R.drawable.weather_type_cloudy_night_2 // Cloud cover 26%-50%
    803 -> R.drawable.weather_type_cloudy_night_3 // Cloud cover 51%-85%
    804 -> R.drawable.weather_type_cloudy // Cloud cover 86%-100%
    else -> R.drawable.weather_type_cloud_unknown // Cloud with question mark for unknown code
}