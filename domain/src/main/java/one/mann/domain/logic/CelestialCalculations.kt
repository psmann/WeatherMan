package one.mann.domain.logic

import one.mann.domain.logic.CelestialBody.*
import one.mann.domain.logic.MeasurementUnit.*
import one.mann.domain.logic.ValueConstraint.*
import java.util.Date
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

/* Created by Psmann. */

/**
 * Constant for angular unit conversion
 * Multiply to convert degrees to radians
 * Divide to convert radians to degrees
 */
const val toRad = PI / 180

/**
 * Constant for lunar cycle calculations
 */
const val LUNAR_CYCLE = 29.530588853
const val HALF_LUNAR_CYCLE = LUNAR_CYCLE / 2

/**
 * Converts given Unix time to Julian Date
 * Reference: https://www.celestialprogramming.com/julian.html
 */
fun unixTimeToJulianDate(unixTime: Long): Double = (unixTime.toDouble() / 86400000) + 2440587.5

/**
 * Reference: https://www.celestialprogramming.com/lowprecisionmoonposition.html
 */
fun calculateLunarRaAndDec(julianDate: Double, outputUnits: MeasurementUnit = RADIAN): RaAndDec {
    // Number of centuries since J2000 epoch
    val t: Double = (julianDate - 2451545) / 36525
    // Ecliptic Longitude
    val lambda: Double = (218.32 + (481267.881 * t) +
        (6.29 * sin((135.0 + 477198.87 * t) * toRad)) - (1.27 * sin((259.3 - 413335.36 * t) * toRad)) +
        (0.66 * sin((235.7 + 890534.22 * t) * toRad)) + (0.21 * sin((269.9 + 954397.74 * t) * toRad)) -
        (0.19 * sin((357.5 + 35999.05 * t) * toRad)) - (0.11 * sin((186.5 + 966404.03 * t) * toRad))) * toRad
    // Ecliptic Latitude
    val beta: Double = ((5.13 * sin((93.3 + 483202.02 * t) * toRad)) + (0.28 * sin((228.2 + 960400.89 * t) * toRad)) -
        (0.28 * sin((318.3 + 6003.15 * t) * toRad)) - (0.17 * sin((217.6 - 407332.21 * t) * toRad))) * toRad
    // Geocentric direction cosines (l, m, n)
    val l: Double = cos(beta) * cos(lambda)
    val m: Double = (0.9175 * cos(beta) * sin(lambda)) - (0.3978 * sin(beta))
    val n: Double = (0.3978 * cos(beta) * sin(lambda)) + (0.9175 * sin(beta))
    val ra: Double = atan2(m, l).constrain(NORMALIZATION_ANGLE_IN_RADIANS)
    val dec: Double = asin(n)

    return RaAndDec(
        rightAscension = if (outputUnits == RADIAN) ra else ra / (15 * toRad),
        declination = if (outputUnits == RADIAN) dec else dec / toRad
    )
}

/**
 * Reference: https://www.celestialprogramming.com/sunPosition-LowPrecisionFromAstronomicalAlmanac.html
 */
fun calculateSolarRaAndDec(julianDate: Double, outputUnits: MeasurementUnit = RADIAN): RaAndDec {
    // Number of days since J2000 epoch
    val n: Double = julianDate - 2451545.0
    // Mean longitude of Sun
    val l: Double = (280.460 + (0.9856474 * n)).constrain(NORMALIZATION_ANGLE_IN_DEGREES)
    // Mean anomaly
    val g: Double = (357.528 + (0.9856003 * n)).constrain(NORMALIZATION_ANGLE_IN_DEGREES)
    // Ecliptic longitude
    val lambda: Double = (l * toRad) + (1.915 * toRad * sin(g * toRad)) + (0.020 * toRad * sin(2 * g * toRad))
    // Obliquity of ecliptic
    val eps: Double = (23.439 - (0.0000004 * n)) * toRad
    val ra: Double = atan2(cos(eps) * sin(lambda), cos(lambda)).constrain(NORMALIZATION_ANGLE_IN_RADIANS)
    val dec: Double = asin(sin(eps) * sin(lambda))

    return RaAndDec(
        rightAscension = if (outputUnits == RADIAN) ra else ra / (15 * toRad),
        declination = if (outputUnits == RADIAN) dec else dec / toRad
    )
}

/**
 * Reference: https://aa.usno.navy.mil/faq/GAST
 * Param @outputUnits takes values [MeasurementUnit.RADIAN] and [MeasurementUnit.HOUR]
 */
fun calculateGreenwichMeanSiderealTime(julianDate: Double, outputUnits: MeasurementUnit): Double {
    // Julian Date at midnight
    val jd0: Double = floor(julianDate) + 0.5
    // Hours since midnight
    val h: Double = (julianDate - jd0) * 24
    // Whole days since J2000 epoch
    val dut: Double = jd0 - 2451545.0
    // Greenwich Mean Sidereal Time in Hours
    val gmst: Double = ((6.697375 + (0.065709824279 * dut) + (1.0027379 * h)) % 24)

    return when (outputUnits) {
        DEGREE -> gmst * 15
        HOUR -> gmst
        RADIAN -> gmst * 15 * toRad
    }
}

/**
 * Reference: https://www.celestialprogramming.com/convert_ra_dec_to_alt_az.html
 * All angular params should be in radians
 * Param @outputUnits takes values [MeasurementUnit.DEGREE] and [MeasurementUnit.RADIAN]
 */
fun calculateAltAndAz(
    ra: Double,
    dec: Double,
    lat: Double,
    lon: Double,
    julianDate: Double,
    outputUnits: MeasurementUnit
): AltAndAz {
    val localSiderealTime: Double = (calculateGreenwichMeanSiderealTime(julianDate, RADIAN) + lon)
    // Hour Angle
    val ha: Double = (localSiderealTime - ra)
    var azimuth: Double = atan2(sin(ha), cos(ha) * sin(lat) - tan(dec) * cos(lat))
    val altitude: Double = asin(sin(lat) * sin(dec) + cos(lat) * cos(dec) * cos(ha))

    azimuth = (azimuth - PI).constrain(NORMALIZATION_ANGLE_IN_RADIANS)

    return AltAndAz(
        altitude = if (outputUnits == RADIAN) altitude else altitude / toRad,
        azimuth = if (outputUnits == RADIAN) azimuth else azimuth / toRad
    )
}

/**
 * Reference: https://www.celestialprogramming.com/risesetalgorithm.html
 */
fun calculateRiseAndSet(
    ra: Double,
    dec: Double,
    lat: Double,
    lon: Double,
    julianDate: Double,
    timeZoneId: String,
    celestialBody: CelestialBody
): RiseAndSet {
    // Apparent rise or set angle of celestial body
    val h0: Double = when (celestialBody) {
        SUN -> -0.8333
        MOON -> 0.125
    }
    // Hour angle
    val ha: Double = acos((sin(h0 * toRad) - sin(lat) * sin(dec)) / (cos(lat) * cos(dec))) / toRad
    val gmstMidnight: Double = calculateGreenwichMeanSiderealTime((floor(julianDate) + 0.5), DEGREE)
    // Use RA and GMST values in degrees instead of hours
    val transit: Double = ((ra / (toRad)) - (lon / toRad) - gmstMidnight) / 360.0
    val timeZoneOffsetInHours = TimeZone.getTimeZone(timeZoneId).getOffset(Date().time).toFloat() / (1000 * 60 * 60)
    // GMT rise and set times in hours
    val gmtRise: Double = (transit - (ha / 360.0)).constrain(BETWEEN_ZERO_AND_ONE) * 24
    val gmtSet: Double = (transit + (ha / 360.0)).constrain(BETWEEN_ZERO_AND_ONE) * 24
    // Local rise and set times calculated by adding time zone offset
    val localRise = (gmtRise + timeZoneOffsetInHours).constrain(NORMALIZATION_HOURS_IN_A_DAY)
    val localSet = (gmtSet + timeZoneOffsetInHours).constrain(NORMALIZATION_HOURS_IN_A_DAY)
    val azRise = acos((sin(dec) + (sin(h0 * toRad) * sin(lat))) / (cos(h0 * toRad) * cos(lat))) / toRad
    val azSet = 360 - azRise

    return RiseAndSet(localRise, localSet, azRise, azSet)
}

/**
 * Returns a celestial body's alt/az at the given local hour.
 */
fun calculateAltAzAtHour(
    hour: Double,
    jdUtcMidnight: Double,
    latRad: Double,
    lonRad: Double,
    celestialBody: CelestialBody,
): AltAndAz {
    val jd = jdUtcMidnight + hour / 24.0
    val rd = when (celestialBody) {
        SUN -> calculateSolarRaAndDec(jd, RADIAN)
        MOON -> calculateLunarRaAndDec(jd, RADIAN)
    }

    return calculateAltAndAz(rd.rightAscension, rd.declination, latRad, lonRad, jd, DEGREE)
}

/**
 * Meridian (transit) = midpoint between rise and set, handling wrap-around.
 */
fun calculateMeridian(
    rise: Double,
    set: Double
): Double {
    return if (set >= rise) (rise + set) / 2.0
    else ((rise + set + 24.0) / 2.0).let { if (it >= 24.0) it - 24.0 else it }
}

/**
 * Altitude along a sine-wave arc.
 * Peaks at [meridianHour] with [peakAlt], troughs 12 h later with [nadirAlt].
 */
fun calculateSineAltitude(
    hour: Double, meridianHour: Double,
    peakAlt: Double, nadirAlt: Double,
): Double {
    val midAlt = (peakAlt + nadirAlt) / 2.0
    val amplitude = (peakAlt - nadirAlt) / 2.0

    return midAlt + amplitude * sin(PI / 12.0 * (hour - meridianHour + 6.0))
}

/**
 * Reference: https://www.celestialprogramming.com/meeus-illuminated_fraction_of_the_moon.html
 */
fun calculateIlluminatedFractionOfMoon(julianDate: Double): Double {
    // Number of centuries since J2000 epoch
    val t: Double = (julianDate - 2451545) / 36525
    // Mean elongation of the Moon
    val d = ((297.8501921 + (445267.1114034 * t) - (0.0018819 * t.pow(2)) + ((t.pow(3)) / 545868.0) -
        ((t.pow(4)) / 113065000.0)).constrain(NORMALIZATION_ANGLE_IN_DEGREES)) * toRad
    // Mean anomaly of the Sun
    val m = ((357.5291092 + (35999.0502909 * t) - (0.0001536 * t.pow(2)) +
        ((t.pow(3)) / 24490000.0)).constrain(NORMALIZATION_ANGLE_IN_DEGREES)) * toRad
    // Mean anomaly of the Moon
    val mp = ((134.9633964 + (477198.8675055 * t) + (0.0087414 * t.pow(2)) + ((t.pow(3)) / 69699.0) -
        ((t * t * t * t) / 14712000.0)).constrain(NORMALIZATION_ANGLE_IN_DEGREES)) * toRad
    // Phase angle
    val i = ((180 - (d / toRad) - (6.289 * sin(mp)) + (2.1 * sin(m)) - (1.274 * sin((2 * d) - mp)) -
        (0.658 * sin(2 * d)) - (0.214 * sin(2 * mp)) - (0.11 * sin(d)))
        .constrain(NORMALIZATION_ANGLE_IN_DEGREES)) * toRad
    // Illuminated fraction
    return (1 + cos(i)) / 2
}

/**
 * Reference: https://www.celestialprogramming.com/snippets/moonage.html
 */
fun calculateMoonAge(julianDate: Double): Double {
    var f = ((julianDate - 2451550.1) / LUNAR_CYCLE) % 1
    f = f.constrain(BETWEEN_ZERO_AND_ONE)

    return (f * LUNAR_CYCLE)
}

/**
 * Normalized various values within desired limits.
 */
fun Double.constrain(type: ValueConstraint): Double = when (type) {
    BETWEEN_ZERO_AND_ONE -> when {
        this < 0 -> this + 1
        this > 1 -> this - 1
        else -> this
    }
    NORMALIZATION_HOURS_IN_A_DAY -> if (this < 0) this + 24 else this
    NORMALIZATION_ANGLE_IN_DEGREES -> (this % 360).let { return if (it < 0) it + 360 else it }
    NORMALIZATION_ANGLE_IN_RADIANS -> if (this < 0) this + (2 * PI) else this
}

/**
 * Converts Moon age in days to a corresponding Moon phase name.
 * The age thresholds are based on the typical lunar cycle of approximately 29.53 days.
 */
fun moonPhaseName(age: Double): String {
    return when {
        age < 1.85 -> "New Moon"
        age < 7.38 -> "Waxing Crescent"
        age < 8.38 -> "First Quarter"
        age < 14.77 -> "Waxing Gibbous"
        age < 16.61 -> "Full Moon"
        age < 22.15 -> "Waning Gibbous"
        age < 23.15 -> "Last Quarter"
        age < 29.53 -> "Waning Crescent"
        else -> "New Moon"
    }
}

/**
 * Converts an azimuth in degrees to a compass direction suffix (N, NE, E, SE, S, SW, W, NW)
 * using 45-degree increments centered on each cardinal/intercardinal direction.
 */
fun azimuthDirection(azDeg: Double): String {
    val normalized = ((azDeg % 360) + 360) % 360
    return when {
        normalized !in 22.5..<337.5 -> "N"
        normalized < 67.5 -> "NE"
        normalized < 112.5 -> "E"
        normalized < 157.5 -> "SE"
        normalized < 202.5 -> "S"
        normalized < 247.5 -> "SW"
        normalized < 292.5 -> "W"
        else -> "NW"
    }
}

/**
 * Converts a time in hours (e.g., 14.5) to a formatted string
 * in 12-hour format with AM/PM (e.g., "2:30 PM").
 */
fun formatHours(h: Double): String {
    val totalMinutes = (h * 60).toInt()
    val hour24 = totalMinutes / 60
    val minutes = totalMinutes % 60
    val period = if (hour24 < 12) "AM" else "PM"
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    return "%d:%02d %s".format(hour12, minutes, period)
}

enum class MeasurementUnit {
    DEGREE, HOUR, RADIAN
}

enum class CelestialBody {
    SUN, MOON
}

enum class ValueConstraint {
    BETWEEN_ZERO_AND_ONE, NORMALIZATION_HOURS_IN_A_DAY, NORMALIZATION_ANGLE_IN_DEGREES, NORMALIZATION_ANGLE_IN_RADIANS
}

/**
 * Stores values for Right Ascension (RA) and Declination (Dec)
 * Parameters must be in Radians for trigonometric calculations
 */
data class RaAndDec(val rightAscension: Double, val declination: Double)

/**
 * Stores values for Altitude and Azimuth
 * Parameters must be in Radians for trigonometric calculations
 */
data class AltAndAz(val altitude: Double, val azimuth: Double)

/**
 * Stores values for Rise and Set times and azimuths
 * Time parameters are in Hours, azimuth parameters are in Degrees
 */
data class RiseAndSet(val rise: Double, val set: Double, val riseAz: Double = 0.0, val setAz: Double = 0.0)
