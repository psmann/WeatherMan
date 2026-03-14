package one.mann.domain.models

/* Created by Psmann. */

data class CelestialInfo(
    val sunAltDeg: Double,
    val sunAzDeg: Double,
    val moonAltDeg: Double,
    val moonAzDeg: Double,
    val sunRise: Double,
    val sunSet: Double,
    val sunRiseAlt: Double,
    val sunSetAlt: Double,
    val sunRiseAz: Double,
    val sunSetAz: Double,
    val moonRise: Double,
    val moonSet: Double,
    val moonRiseAlt: Double,
    val moonSetAlt: Double,
    val moonRiseAz: Double,
    val moonSetAz: Double,
    val moonAge: Double,
    val moonIllumination: Double,
    val currentHourFraction: Double,
    val sunMeridian: Double,
    val sunPeakAlt: Double,
    val sunMeridianAz: Double,
    val sunNadirHour: Double,
    val sunNadirAlt: Double,
    val sunNadirAz: Double,
    val moonMeridian: Double,
    val moonPeakAlt: Double,
    val moonMeridianAz: Double,
    val moonNadirHour: Double,
    val moonNadirAlt: Double,
    val moonNadirAz: Double,
) {
    /** Whether the sun is currently above the horizon. */
    val isDaytime: Boolean
        get() = if (sunRise < sunSet)
            currentHourFraction in sunRise..sunSet
        else
            currentHourFraction >= sunRise || currentHourFraction <= sunSet
}
