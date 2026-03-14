package one.mann.interactors.data.repositories

import one.mann.domain.logic.CelestialBody.MOON
import one.mann.domain.logic.CelestialBody.SUN
import one.mann.domain.logic.MeasurementUnit
import one.mann.domain.logic.ValueConstraint
import one.mann.domain.logic.calculateAltAndAz
import one.mann.domain.logic.calculateAltAzAtHour
import one.mann.domain.logic.calculateIlluminatedFractionOfMoon
import one.mann.domain.logic.calculateLunarRaAndDec
import one.mann.domain.logic.calculateMeridian
import one.mann.domain.logic.calculateMoonAge
import one.mann.domain.logic.calculateRiseAndSet
import one.mann.domain.logic.calculateSolarRaAndDec
import one.mann.domain.logic.constrain
import one.mann.domain.logic.toRad
import one.mann.domain.logic.unixTimeToJulianDate
import one.mann.domain.models.CelestialInfo
import java.util.TimeZone
import javax.inject.Inject
import kotlin.math.floor

/* Created by Psmann. */

class CelestialRepository @Inject constructor() {

    fun compute(
        latDeg: Double,
        lonDeg: Double,
        timeZoneId: String,
        currentTimeMillis: Long,
    ): CelestialInfo {
        val latRad = latDeg * toRad
        val lonRad = lonDeg * toRad
        val currentJd = unixTimeToJulianDate(currentTimeMillis)

        val tzOffsetDays = TimeZone.getTimeZone(timeZoneId)
            .getOffset(currentTimeMillis).toDouble() / (1000.0 * 60 * 60 * 24)

        val jdLocalMidnight = floor(currentJd + tzOffsetDays - 0.5) + 0.5
        val jdUtcMidnight = jdLocalMidnight - tzOffsetDays
        val currentHourFraction = ((currentJd - jdUtcMidnight) * 24.0).coerceIn(0.0, 24.0)

        // Current alt/az
        val sunRaDec = calculateSolarRaAndDec(currentJd, MeasurementUnit.RADIAN)
        val moonRaDec = calculateLunarRaAndDec(currentJd, MeasurementUnit.RADIAN)
        val sunAltAz = calculateAltAndAz(
            sunRaDec.rightAscension, sunRaDec.declination,
            latRad, lonRad, currentJd, MeasurementUnit.DEGREE,
        )
        val moonAltAz = calculateAltAndAz(
            moonRaDec.rightAscension, moonRaDec.declination,
            latRad, lonRad, currentJd, MeasurementUnit.DEGREE,
        )

        // Rise / set
        val jdMidnight = floor(currentJd) + 0.5
        val sunRaDecMid = calculateSolarRaAndDec(jdMidnight, MeasurementUnit.RADIAN)
        val moonRaDecMid = calculateLunarRaAndDec(jdMidnight, MeasurementUnit.RADIAN)
        val sunRS = calculateRiseAndSet(
            sunRaDecMid.rightAscension, sunRaDecMid.declination,
            latRad, lonRad, jdMidnight, timeZoneId, SUN,
        )
        val moonRS = calculateRiseAndSet(
            moonRaDecMid.rightAscension, moonRaDecMid.declination,
            latRad, lonRad, jdMidnight, timeZoneId, MOON,
        )

        // Meridian & nadir hours
        val sunMeridian = calculateMeridian(sunRS.rise, sunRS.set)
        val moonMeridian = calculateMeridian(moonRS.rise, moonRS.set)
        val sunNadirHour = (sunMeridian + 12.0).constrain(ValueConstraint.NORMALIZATION_HOURS_IN_A_DAY)
        val moonNadirHour = (moonMeridian + 12.0).constrain(ValueConstraint.NORMALIZATION_HOURS_IN_A_DAY)

        // Alt/az at key hours
        val sunAtMeridian = calculateAltAzAtHour(sunMeridian, jdUtcMidnight, latRad, lonRad, SUN)
        val moonAtMeridian = calculateAltAzAtHour(moonMeridian, jdUtcMidnight, latRad, lonRad, MOON)
        val sunAtNadir = calculateAltAzAtHour(sunNadirHour, jdUtcMidnight, latRad, lonRad, SUN)
        val moonAtNadir = calculateAltAzAtHour(moonNadirHour, jdUtcMidnight, latRad, lonRad, MOON)
        val sunAtRise = calculateAltAzAtHour(sunRS.rise, jdUtcMidnight, latRad, lonRad, SUN)
        val sunAtSet = calculateAltAzAtHour(sunRS.set, jdUtcMidnight, latRad, lonRad, SUN)
        val moonAtRise = calculateAltAzAtHour(moonRS.rise, jdUtcMidnight, latRad, lonRad, MOON)
        val moonAtSet = calculateAltAzAtHour(moonRS.set, jdUtcMidnight, latRad, lonRad, MOON)

        return CelestialInfo(
            sunAltDeg = sunAltAz.altitude,
            sunAzDeg = sunAltAz.azimuth,
            moonAltDeg = moonAltAz.altitude,
            moonAzDeg = moonAltAz.azimuth,
            sunRise = sunRS.rise,
            sunSet = sunRS.set,
            sunRiseAlt = sunAtRise.altitude,
            sunSetAlt = sunAtSet.altitude,
            sunRiseAz = sunRS.riseAz,
            sunSetAz = sunRS.setAz,
            moonRise = moonRS.rise,
            moonSet = moonRS.set,
            moonRiseAlt = moonAtRise.altitude,
            moonSetAlt = moonAtSet.altitude,
            moonRiseAz = moonRS.riseAz,
            moonSetAz = moonRS.setAz,
            moonAge = calculateMoonAge(currentJd),
            moonIllumination = calculateIlluminatedFractionOfMoon(currentJd),
            currentHourFraction = currentHourFraction,
            sunMeridian = sunMeridian,
            sunPeakAlt = sunAtMeridian.altitude,
            sunMeridianAz = sunAtMeridian.azimuth,
            sunNadirHour = sunNadirHour,
            sunNadirAlt = sunAtNadir.altitude,
            sunNadirAz = sunAtNadir.azimuth,
            moonMeridian = moonMeridian,
            moonPeakAlt = moonAtMeridian.altitude,
            moonMeridianAz = moonAtMeridian.azimuth,
            moonNadirHour = moonNadirHour,
            moonNadirAlt = moonAtNadir.altitude,
            moonNadirAz = moonAtNadir.azimuth,
        )
    }
}
