package one.mann.weatherman.ui.common.util

import one.mann.weatherman.R
import one.mann.weatherman.api.openweathermap.dayIcons
import one.mann.weatherman.api.openweathermap.nightIcons

/** set background depending upon time of day and weather conditions */
internal fun getBackgroundGradient(sunPosition: Float, isCloudy: Boolean): Int = when (sunPosition) {
    in -0.1..0.15, in 0.85..1.1 ->
        if (isCloudy) R.drawable.background_gradient_sunrise_clouds
        else R.drawable.background_gradient_sunrise_clear
    in 0.16..0.84 ->
        if (isCloudy) R.drawable.background_gradient_day_clouds
        else R.drawable.background_gradient_day_clear
    else ->
        if (isCloudy) R.drawable.background_gradient_night_clouds
        else R.drawable.background_gradient_night_clear
}

/** Get file name for vector resource */
internal fun getUri(iconCode: Int, sunPosition: Float): String =
        if (sunPosition in 0.0..1.0) dayIcons(iconCode) else nightIcons(iconCode)