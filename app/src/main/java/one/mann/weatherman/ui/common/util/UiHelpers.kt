package one.mann.weatherman.ui.common.util

import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons

/* Created by Psmann. */

/** Set layout background depending upon time of day and weather conditions */
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

/** Get file name for vector resource */
internal fun getUri(code: Int, sunPosition: Float): String {
    return if (sunPosition in 0.0..1.0) dayIcons(code) else nightIcons(code)
}