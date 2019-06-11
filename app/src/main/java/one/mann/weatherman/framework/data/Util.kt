package one.mann.weatherman.framework.data

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

internal const val FLAG_OFFSET = 0x1F1E6 // Regional Indicator Symbol for letter A
internal const val ASCII_OFFSET = 0x41 // Uppercase letter A
internal const val HOURS_PATTERN = "H 'Hours and' m 'Minutes'"
internal const val DATE_PATTERN = "d MMM, h:mm aa"
internal const val DAY_PATTERN = "E"
internal const val TIME_PATTERN = "h:mm aa"
internal const val UNITS = "metric"
internal const val ICON_EXTENSION = ".png"
internal const val CELSIUS = " C"
internal const val HECTOPASCAL = " hPa"
internal const val PERCENT = " %"
internal const val METERS = " m"
internal const val METERS_PER_SECOND = " m/s"
internal const val DEGREES = " °"

internal fun countryCodeToEmoji(code: String): String {
    val firstChar = Character.codePointAt(code, 0) - ASCII_OFFSET + FLAG_OFFSET
    val secondChar = Character.codePointAt(code, 1) - ASCII_OFFSET + FLAG_OFFSET
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

internal fun lengthOfDay(sunrise: Long, sunset: Long): String {
    val hoursFormat = SimpleDateFormat(HOURS_PATTERN, Locale.getDefault())
    hoursFormat.timeZone = TimeZone.getTimeZone("UTC") // Removes time offset
    return hoursFormat.format(Date((sunset - sunrise) * 1000)).toString() // Convert to nanosecond
}

internal fun timeInMinutes(time: Long, timeZone: String): Float {
    val hourFormat = SimpleDateFormat("H", Locale.getDefault())
    val minuteFormat = SimpleDateFormat("m", Locale.getDefault())
    hourFormat.timeZone = TimeZone.getTimeZone(timeZone)
    minuteFormat.timeZone = TimeZone.getTimeZone(timeZone)
    return (hourFormat.format(Date(time)).toFloat() * 60) + minuteFormat.format(Date(time)).toFloat()
}

internal fun unixToDate(time: Long, timezone: TimeZone = TimeZone.getDefault()): String {
    val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    dateFormat.timeZone = timezone
    return dateFormat.format(Date(time)).toString()
}

internal fun unixToDay(time: Long): String {
    val dayFormat = SimpleDateFormat(DAY_PATTERN, Locale.getDefault())
    return dayFormat.format(Date(time)).toString()
}

internal fun unixToTime(time: Long, timezone: TimeZone = TimeZone.getDefault()): String {
    val timeFormat = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
    timeFormat.timeZone = timezone
    return timeFormat.format(Date(time)).toString()
}

internal fun sunPositionBias(sunrise: Float, sunset: Float, currentTime: Float): Float =
        (currentTime - sunrise) / (sunset - sunrise)

internal fun feelsLike(temperature: Float, humidity: Int, wind: Float): Float {
    // Using https://blog.metservice.com/FeelsLikeTemp for reference
    var feelsLike: Double
    when {
        temperature < 14 -> { // = Wind Chill using JAG/TI formula
            val k = Math.pow(wind.toDouble() * 3.6, 0.16) // Wind speed converted to km/h and raised to the power
            feelsLike = 13.12 + (0.6215 * temperature) - (11.35 * k) + (0.396 * temperature * k)
            if (temperature > 10) // Roll-over Zone
                feelsLike = temperature - (((temperature - feelsLike) * (14 - temperature)) / 4)
        }
        else -> { // = Heat Index or Apparent Temperature using Steadman's formula
            val e = humidity / 100 * 6.105 * Math.exp(17.27 * temperature / (237.7 + temperature)) // Pressure
            feelsLike = temperature + (0.33 * e) - (0.7 * wind) - 4
        }
    }
    return BigDecimal.valueOf(feelsLike)
            .setScale(2, RoundingMode.HALF_UP).toFloat() // Set precision to match current temp
}