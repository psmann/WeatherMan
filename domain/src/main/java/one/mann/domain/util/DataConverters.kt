package one.mann.domain.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt

const val FLAG_OFFSET = 0x1F1E6 // Regional Indicator Symbol for letter A
const val ASCII_OFFSET = 0x41 // Uppercase letter A
const val HOURS_PATTERN = "H 'Hours and' m 'Minutes'"
const val DATE_PATTERN = "d MMM, h:mm aa"
const val DAY_PATTERN = "E"
const val TIME_PATTERN = "h:mm aa"
const val UNITS = "metric"
const val CELSIUS = " C"
const val HECTOPASCAL = " hPa"
const val PERCENT = " %"
const val METERS = " m"
const val METERS_PER_SECOND = " m/s"
const val DEGREES = " °"

// Append data with units
fun Any.addUnits(units: String): String = this.toString() + units

// Round off variable to nearest integer value and return as a string
fun Float.roundOff(): String = this.roundToInt().toString()

// Convert location coordinates into a truncated comma separated string
fun Array<Float>.coordinatesInString(): String =
        String.format("%.4f", this[0]) + ", " + String.format("%.4f", this[1])

// Convert the country code into an emoji icon
fun countryCodeToEmoji(code: String): String {
    val firstChar = Character.codePointAt(code, 0) - ASCII_OFFSET + FLAG_OFFSET
    val secondChar = Character.codePointAt(code, 1) - ASCII_OFFSET + FLAG_OFFSET
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

// Calculate the length of day using sunrise and sunset
fun lengthOfDay(sunrise: Long, sunset: Long): String {
    val hoursFormat = SimpleDateFormat(HOURS_PATTERN, Locale.getDefault())
    hoursFormat.timeZone = TimeZone.getTimeZone("UTC") // Remove time offset
    return hoursFormat.format(Date(sunset - sunrise)).toString()
}

// Convert unix epoch time into minutes
fun epochToMinutes(time: Long, timezone: String): Float {
    val hourFormat = SimpleDateFormat("H", Locale.getDefault())
    val minuteFormat = SimpleDateFormat("m", Locale.getDefault())
    hourFormat.timeZone = TimeZone.getTimeZone(timezone)
    minuteFormat.timeZone = TimeZone.getTimeZone(timezone)
    return (hourFormat.format(Date(time)).toFloat() * 60) + minuteFormat.format(Date(time)).toFloat()
}

// Convert unix epoch time into date
fun epochToDate(time: Long, timezone: String): String {
    val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone(timezone)
    return dateFormat.format(Date(time)).toString()
}

// Convert unix epoch time into day
fun epochToDay(time: Long): String {
    val dayFormat = SimpleDateFormat(DAY_PATTERN, Locale.getDefault())
    return dayFormat.format(Date(time)).toString()
}

// Convert unix epoch time into normal time (hours and minutes)
fun epochToTime(time: Long, timezone: String): String {
    val timeFormat = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
    timeFormat.timeZone = TimeZone.getTimeZone(timezone)
    return timeFormat.format(Date(time)).toString()
}

// Calculate the sun position to be used in a SunGraphView
fun sunPositionBias(sunrise: Float, sunset: Float, currentTime: Float): Float =
        (currentTime - sunrise) / (sunset - sunrise)

// Calculate feelsLike temperature using https://blog.metservice.com/FeelsLikeTemp for reference
fun feelsLike(temperature: Float, humidity: Float, wind: Float): Float {
    var feelsLike: Double
    when {
        temperature < 14 -> { // = Wind Chill using JAG/TI formula
            val k = (wind.toDouble() * 3.6).pow(0.16) // Wind speed converted to km/h and raised to the power
            feelsLike = 13.12 + (0.6215 * temperature) - (11.35 * k) + (0.396 * temperature * k)
            if (temperature > 10) // Roll-over Zone
                feelsLike = temperature - (((temperature - feelsLike) * (14 - temperature)) / 4)
        }
        else -> { // = Heat Index or Apparent Temperature using Steadman's formula
            val e = humidity.toInt() / 100 * 6.105 * exp(17.27 * temperature / (237.7 + temperature)) // Pressure
            feelsLike = temperature + (0.33 * e) - (0.7 * wind) - 4
        }
    }
    return BigDecimal.valueOf(feelsLike)
            .setScale(2, RoundingMode.HALF_UP)
            .toFloat() // Set precision to match current temp
}