package one.mann.domain.logic

import one.mann.domain.model.Location
import one.mann.domain.model.UnitsType
import one.mann.domain.model.UnitsType.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt

/** Append data with units */
fun Any.addSuffix(units: String): String = this.toString() + units

/** Set units to Imperial or Metric when data is fetched from API */
fun Float.setUnits(units: String, type: UnitsType) = when (type) {
    TEMPERATURE -> String.format("%.1f", if (units == IMPERIAL) ((this * 1.8) + 32) else this).toFloat()
    WIND -> String.format("%.1f", if (units == IMPERIAL) (this * 2.237) else (this * 3.6)).toFloat()
    VISIBILITY -> String.format("%.1f", if (units == IMPERIAL) (this / 1609.344) else (this / 1000)).toFloat()
}

/** Change units to Imperial or Metric when changed from Settings */
fun Float.changeUnits(units: String, type: UnitsType) = when (type) {
    TEMPERATURE -> String.format("%.1f", if (units == IMPERIAL) ((this * 1.8) + 32) else (this - 32) * 0.556).toFloat()
    WIND -> String.format("%.1f", if (units == IMPERIAL) (this / 1.609) else (this * 1.609)).toFloat()
    VISIBILITY -> String.format("%.1f", if (units == IMPERIAL) (this / 1.609) else (this * 1.609)).toFloat()
}

/** Round off variable to nearest integer value and return as a string */
fun Float.roundOff(): String = this.roundToInt().toString()

/** Truncate location coordinates to 4 decimal places */
fun Location.truncate(): Location = Location(listOf(String.format("%.4f", coordinates[0]).toFloat(),
        String.format("%.4f", coordinates[1]).toFloat()), id)

/** Convert location coordinates into a truncated comma separated string */
fun List<Float>.coordinatesInString(): String = String.format("%.4f", this[0]) + ", " + String.format("%.4f", this[1])

/** Convert the country code into an emoji icon */
fun countryCodeToEmoji(code: String): String {
    val firstChar = Character.codePointAt(code, 0) - ASCII_OFFSET + FLAG_OFFSET
    val secondChar = Character.codePointAt(code, 1) - ASCII_OFFSET + FLAG_OFFSET
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

/** Base function to convert Unix epoch time into regular time formats */
private fun epochToFormat(time: Long, timezone: String, pattern: String): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    format.timeZone = if (timezone == "") TimeZone.getDefault() else TimeZone.getTimeZone(timezone)
    return format.format(Date(time)).toString()
}

/** Calculate the length of day using sunrise and sunset */
fun lengthOfDay(sunrise: Long, sunset: Long): String {
    val hoursFormat = SimpleDateFormat(DURATION_PATTERN, Locale.getDefault())
    hoursFormat.timeZone = TimeZone.getTimeZone("UTC") // Remove time offset
    return hoursFormat.format(Date(sunset - sunrise)).toString()
}

/** Convert Unix epoch time into minutes */
fun epochToMinutes(time: Long, timezone: String): Float {
    val hourFormat = SimpleDateFormat("H", Locale.getDefault())
    val minuteFormat = SimpleDateFormat("m", Locale.getDefault())
    hourFormat.timeZone = if (timezone == "") TimeZone.getDefault() else TimeZone.getTimeZone(timezone)
    minuteFormat.timeZone = if (timezone == "") TimeZone.getDefault() else TimeZone.getTimeZone(timezone)
    return (hourFormat.format(Date(time)).toFloat() * 60) + minuteFormat.format(Date(time)).toFloat()
}

/** Convert Unix epoch time into date */
fun epochToDate(time: Long, timezone: String): String = epochToFormat(time, timezone, DATE_PATTERN)

/** Convert Unix epoch time into day */
fun epochToDay(time: Long, timezone: String): String = epochToFormat(time, timezone, DAY_PATTERN)

/** Convert Unix epoch time into hour */
fun epochToHour(time: Long, timezone: String): String = epochToFormat(time, timezone, HOUR_PATTERN)

/** Convert Unix epoch time into normal time (hours and minutes) */
fun epochToTime(time: Long, timezone: String): String = epochToFormat(time, timezone, TIME_PATTERN)

/** Calculate the sun position to be used in SunGraphView and weather icons */
fun sunPositionBias(sunrise: Float, sunset: Float, time: Float): Float = (time - sunrise) / (sunset - sunrise)

/** Calculate FeelsLike temperature using https://blog.metservice.com/FeelsLikeTemp for reference */
fun feelsLike(temperature: Float, humidity: Int, wind: Float): Float {
    var feelsLike: Double
    if (temperature < 14) { // = Wind Chill calculated using JAG/TI formula
        val k = (wind.toDouble() * 3.6).pow(0.16) // Wind speed converted to km/h and raised to the power
        feelsLike = 13.12 + (0.6215 * temperature) - (11.35 * k) + (0.396 * temperature * k)
        if (temperature > 10) feelsLike = temperature - (((temperature - feelsLike) * (14 - temperature)) / 4) // = Roll-over
    } else { // = Heat Index or Apparent Temperature calculated using the Steadman formula
        val e = humidity / 100 * 6.105 * exp(17.27 * temperature / (237.7 + temperature)) // Pressure
        feelsLike = temperature + (0.33 * e) - (0.7 * wind) - 4
    }
    return String.format("%.1f", feelsLike).toFloat() // Set precision to match current temp
}