package one.mann.weatherman.ui.common.util

import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons

/** Set layout background depending upon time of day and weather conditions */
internal fun getGradient(sunPosition: Float, isOvercast: Boolean): Int = when (sunPosition) {
    in -0.05..0.10, in 0.90..1.05 -> // Dawn-Sunrise and Sunset-Twilight
        if (isOvercast) R.drawable.background_gradient_sunrise_clouds
        else R.drawable.background_gradient_sunrise_clear
    in 0.11..0.89 -> // Day
        if (isOvercast) R.drawable.background_gradient_day_clouds
        else R.drawable.background_gradient_day_clear
    else -> // Night
        if (isOvercast) R.drawable.background_gradient_night_clouds
        else R.drawable.background_gradient_night_clear
}

/** Get file name for vector resource */
internal fun getUri(iconCode: Int, sunPosition: Float): String =
        if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)