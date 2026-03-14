package one.mann.weatherman.ui.common.models

/* Created by Psmann. */

internal data class CelestialInfo(
    // Formatted display strings for Sun info card
    val sunAltitude: String = "",
    val sunAzimuth: String = "",
    val sunRiseInfo: String = "",
    val sunSetInfo: String = "",
    val sunZenithInfo: String = "",
    val sunNadirInfo: String = "",
    // Formatted display strings for Moon info card
    val moonAltitude: String = "",
    val moonAzimuth: String = "",
    val moonRiseInfo: String = "",
    val moonSetInfo: String = "",
    val moonZenithInfo: String = "",
    val moonNadirInfo: String = "",
    val moonPhase: String = "",
    val moonAge: String = "",
    val moonIllumination: String = "",
    // Raw values for chart rendering and screen logic
    val sunAltDeg: Double = 0.0,
    val sunAzDeg: Double = 0.0,
    val moonAltDeg: Double = 0.0,
    val moonAzDeg: Double = 0.0,
    val sunRiseHour: Double = 0.0,
    val sunSetHour: Double = 0.0,
    val sunRiseAz: Double = 0.0,
    val sunSetAz: Double = 0.0,
    val moonRiseHour: Double = 0.0,
    val moonSetHour: Double = 0.0,
    val moonRiseAz: Double = 0.0,
    val moonSetAz: Double = 0.0,
    val moonAgeDays: Double = 0.0,
    val moonIlluminationFraction: Double = 0.0,
    val currentHourFraction: Double = 0.0,
    val sunMeridianHour: Double = 0.0,
    val sunPeakAlt: Double = 0.0,
    val sunNadirAlt: Double = 0.0,
    val moonMeridianHour: Double = 0.0,
    val moonPeakAlt: Double = 0.0,
    val moonNadirAlt: Double = 0.0,
    val isDaytime: Boolean = false,
)
