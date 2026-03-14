package one.mann.weatherman.ui.celestial.chart

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import one.mann.domain.logic.HALF_LUNAR_CYCLE
import one.mann.domain.logic.calculateSineAltitude
import one.mann.weatherman.ui.common.models.CelestialInfo
import one.mann.weatherman.ui.celestial.chart.ChartConfig.ALT_MAX
import one.mann.weatherman.ui.celestial.chart.ChartConfig.ALT_MIN
import one.mann.weatherman.ui.celestial.chart.ChartConfig.ALT_RANGE
import one.mann.weatherman.ui.celestial.chart.ChartConfig.BOTTOM_PAD
import one.mann.weatherman.ui.celestial.chart.ChartConfig.CURVE_STEPS
import one.mann.weatherman.ui.celestial.chart.ChartConfig.LEFT_PAD
import one.mann.weatherman.ui.celestial.chart.ChartConfig.RIGHT_PAD
import one.mann.weatherman.ui.celestial.chart.ChartConfig.STAR_COUNT
import one.mann.weatherman.ui.celestial.chart.ChartConfig.TOP_PAD
import one.mann.weatherman.ui.celestial.theme.CelestialColors
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/* Created by Psmann. */

/**
 * Main sky-chart drawing entry point.
 */
internal fun DrawScope.drawSky(
    groundColor: Color,
    info: CelestialInfo,
    azimuthMode: Boolean,
) {
    val w = size.width
    val h = size.height
    val chartW = w - LEFT_PAD - RIGHT_PAD
    val chartH = h - TOP_PAD - BOTTOM_PAD
    val horizonY = TOP_PAD + chartH * (1f - ((0.0 - ALT_MIN) / ALT_RANGE).toFloat())

    drawBackground(w, h, horizonY, groundColor, info.sunAltDeg)
    drawGridAndLabels(w, chartH, horizonY, azimuthMode)
    drawLegend()

    if (azimuthMode) {
        drawAzimuthGraph(chartW, horizonY, info)
    } else {
        drawTimeGraph(chartW, horizonY, info)
    }
}

/**
 * Draws a programmatic moon-phase disc.
 */
internal fun DrawScope.drawMoonDisc(
    cx: Float, cy: Float, moonR: Float,
    illumination: Float, waxing: Boolean,
) {
    val discRect = Rect(cx - moonR, cy - moonR, cx + moonR, cy + moonR)

    // Full white disc
    drawCircle(CelestialColors.MoonDiscLit, radius = moonR, center = Offset(cx, cy))

    // Dark shadow region on top
    val termXSigned = moonR * cos(PI.toFloat() * illumination)
    val isGibbous = termXSigned < 0f
    val termXRadius = abs(termXSigned)

    if (illumination < 0.98f) {
        val shadow = Path()
        if (illumination < 0.02f) {
            shadow.addOval(discRect)
        } else {
            shadow.addArc(discRect, if (waxing) 90f else -90f, 180f)
            shadow.arcTo(
                Rect(cx - termXRadius, cy - moonR, cx + termXRadius, cy + moonR),
                if (waxing) -90f else 90f,
                if (isGibbous) -180f else 180f,
                forceMoveTo = false,
            )
            shadow.close()
        }
        drawPath(shadow, color = CelestialColors.MoonDiscDark)
    }

    // Outline
    drawCircle(Color.White.copy(alpha = 0.55f), moonR, Offset(cx, cy), style = Stroke(2f))
}

private fun altToY(alt: Double, chartH: Float): Float {
    val fraction = (alt.coerceIn(ALT_MIN, ALT_MAX) - ALT_MIN) / ALT_RANGE
    return TOP_PAD + chartH * (1f - fraction.toFloat())
}

/**
 * Shared marker: dot + label text.
 */
private fun DrawScope.drawMarker(
    x: Float, y: Float, label: String,
    dotColor: Color, paint: Paint, labelDy: Float,
) {
    drawCircle(dotColor, radius = 8f, center = Offset(x, y))
    drawContext.canvas.nativeCanvas.drawText(label, x, y + labelDy, paint)
}

private fun DrawScope.drawBackground(
    w: Float,
    h: Float,
    horizonY: Float,
    groundColor: Color,
    sunAltDeg: Double,
) {
    // Only draw stars at night
    if (sunAltDeg < -6) {
        ChartPaints.starRandom.setSeed(42L)
        repeat(STAR_COUNT) {
            val sx = ChartPaints.starRandom.nextFloat() * w
            val sy = ChartPaints.starRandom.nextFloat() * horizonY * 0.95f
            val r = 0.8f + ChartPaints.starRandom.nextFloat() * 1.5f
            drawCircle(Color.White.copy(alpha = 0.4f + ChartPaints.starRandom.nextFloat() * 0.4f), r, Offset(sx, sy))
        }
    }
    // Ground area below horizon keeps its colour
    drawRect(
        brush = Brush.verticalGradient(listOf(groundColor, groundColor), startY = horizonY, endY = h),
        topLeft = Offset(0f, horizonY), size = Size(w, h - horizonY),
    )
}

private fun DrawScope.drawGridAndLabels(w: Float, chartH: Float, horizonY: Float, azimuthMode: Boolean) {
    val nc = drawContext.canvas.nativeCanvas

    // Horizon (0° line)
    drawLine(
        CelestialColors.HorizonGreen,
        Offset(LEFT_PAD, horizonY),
        Offset(w - RIGHT_PAD, horizonY),
        strokeWidth = 4f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f)),
    )
    nc.drawText("0°", LEFT_PAD - 6f, horizonY + 8f, ChartPaints.horizonLabel)

    // Altitude degree labels (boundary lines at ±90°)
    for (deg in intArrayOf(-90, -60, -30, 30, 60, 90)) {
        val y = altToY(deg.toDouble(), chartH)
        if (deg == -90 || deg == 90) {
            drawLine(Color.White.copy(alpha = 0.15f), Offset(LEFT_PAD, y), Offset(w - RIGHT_PAD, y), strokeWidth = 2f)
        }
        nc.drawText("${deg}°", LEFT_PAD - 6f, y + 10f, ChartPaints.altGuide)
    }

    // East / West orientation labels and graph title
    val labelY = TOP_PAD + chartH + 40f
    nc.drawText("E", LEFT_PAD + 10f, labelY, ChartPaints.timeLabel)
    nc.drawText("W", w - RIGHT_PAD - 10f, labelY, ChartPaints.timeLabel)
    val titleText = if (azimuthMode) "Horizon" else "24 Hours"
    nc.drawText(titleText, (LEFT_PAD + w - RIGHT_PAD) / 2f, labelY, ChartPaints.graphTitle)
}

private fun DrawScope.drawLegend() {
    val y = TOP_PAD + 18f
    drawLine(
        CelestialColors.SunColor.copy(alpha = 0.5f),
        Offset(LEFT_PAD + 8f, y),
        Offset(LEFT_PAD + 40f, y),
        strokeWidth = 6f
    )
    drawContext.canvas.nativeCanvas.drawText("Sun", LEFT_PAD + 48f, y + 8f, ChartPaints.sunLegend)
    drawLine(
        CelestialColors.MoonColor.copy(alpha = 0.4f),
        Offset(LEFT_PAD + 120f, y),
        Offset(LEFT_PAD + 152f, y),
        strokeWidth = 5f
    )
    drawContext.canvas.nativeCanvas.drawText("Moon", LEFT_PAD + 160f, y + 8f, ChartPaints.moonLegend)
}

private fun DrawScope.drawAzimuthGraph(
    chartW: Float, horizonY: Float,
    info: CelestialInfo,
) {
    val chartH = size.height - TOP_PAD - BOTTOM_PAD
    val azMin = minOf(info.sunRiseAz, info.moonRiseAz) - 5.0
    val azMax = maxOf(info.sunSetAz, info.moonSetAz) + 5.0
    val azRange = if (azMax > azMin && azMax.isFinite() && azMin.isFinite()) azMax - azMin else 360.0

    fun azToX(az: Double): Float = LEFT_PAD + (((az - azMin) / azRange) * chartW).toFloat()
    fun altY(alt: Double): Float = altToY(alt, chartH)

    // Sun arc
    drawAzAltArc(
        ::azToX,
        ::altY,
        info.sunRiseAz,
        info.sunSetAz,
        info.sunPeakAlt,
        CelestialColors.SunColor.copy(alpha = 0.5f),
        6f
    )
    // Moon arc
    drawAzAltArc(
        ::azToX,
        ::altY,
        info.moonRiseAz,
        info.moonSetAz,
        info.moonPeakAlt,
        CelestialColors.MoonColor.copy(alpha = 0.4f),
        5f
    )
    // Rise / set / meridian markers
    drawAzimuthMarkers(info, horizonY, ::azToX, ::altY)

    // Sun marker — only if above horizon
    if (info.sunAltDeg >= 0) {
        val sunFrac = ((info.sunAzDeg - info.sunRiseAz) / (info.sunSetAz - info.sunRiseAz)).coerceIn(0.0, 1.0)
        val sunCurveAlt = info.sunPeakAlt * sin(PI * sunFrac)
        drawSunMarker(azToX(info.sunAzDeg), altY(sunCurveAlt))
    }

    // Moon disc — only if above horizon
    if (info.moonAltDeg >= 0) {
        val moonFrac = ((info.moonAzDeg - info.moonRiseAz) / (info.moonSetAz - info.moonRiseAz)).coerceIn(0.0, 1.0)
        val moonCurveAlt = info.moonPeakAlt * sin(PI * moonFrac)
        drawMoonDisc(
            azToX(info.moonAzDeg),
            altY(moonCurveAlt),
            22f,
            info.moonIlluminationFraction.toFloat(),
            info.moonAgeDays < HALF_LUNAR_CYCLE
        )
    }
}

private fun DrawScope.drawTimeGraph(
    chartW: Float, horizonY: Float,
    info: CelestialInfo,
) {
    val chartH = size.height - TOP_PAD - BOTTOM_PAD
    fun timeToX(hour: Double): Float = LEFT_PAD + ((hour / 23.0) * chartW).toFloat()
    fun altY(alt: Double): Float = altToY(alt, chartH)

    // Sine-wave arcs (full 24h)
    drawSineArc(
        ::timeToX,
        ::altY,
        info.sunMeridianHour,
        info.sunPeakAlt,
        info.sunNadirAlt,
        CelestialColors.SunColor.copy(alpha = 0.5f),
        6f
    )
    drawSineArc(
        ::timeToX,
        ::altY,
        info.moonMeridianHour,
        info.moonPeakAlt,
        info.moonNadirAlt,
        CelestialColors.MoonColor.copy(alpha = 0.4f),
        5f
    )

    // Rise / set / meridian markers
    drawTimeMarkers(info, horizonY, ::timeToX, ::altY)

    // Sun marker at current time
    val nowX = timeToX(info.currentHourFraction)
    val sunCurveAlt =
        calculateSineAltitude(info.currentHourFraction, info.sunMeridianHour, info.sunPeakAlt, info.sunNadirAlt)
    val sunY = altY(sunCurveAlt)
    drawSunMarker(nowX, sunY)

    // Moon disc at current time (always on the curve)
    val moonCurveAlt =
        calculateSineAltitude(info.currentHourFraction, info.moonMeridianHour, info.moonPeakAlt, info.moonNadirAlt)
    val moonY = altY(moonCurveAlt)
    drawMoonDisc(nowX, moonY, 22f, info.moonIlluminationFraction.toFloat(), info.moonAgeDays < HALF_LUNAR_CYCLE)
}

/**
 * Half-sine arc: 0 at riseAz → peak → 0 at setAz.
 */
private fun DrawScope.drawAzAltArc(
    azToX: (Double) -> Float, altToY: (Double) -> Float,
    riseAz: Double, setAz: Double, peakAlt: Double,
    color: Color, strokeWidth: Float,
) {
    val path = Path()
    for (i in 0..CURVE_STEPS) {
        val fraction = i.toDouble() / CURVE_STEPS
        val az = riseAz + fraction * (setAz - riseAz)
        val alt = peakAlt * sin(PI * fraction)
        val x = azToX(az)
        val y = altToY(alt)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    drawPath(path, color = color, style = Stroke(width = strokeWidth))
}

/**
 * Time-based sine-wave arc for the 24-hour graph.
 */
private fun DrawScope.drawSineArc(
    timeToX: (Double) -> Float, altToY: (Double) -> Float,
    meridian: Double, peakAlt: Double, nadirAlt: Double,
    color: Color, strokeWidth: Float,
) {
    val path = Path()
    for (i in 0..CURVE_STEPS) {
        val t = i * 23.0 / CURVE_STEPS
        val x = timeToX(t)
        val y = altToY(calculateSineAltitude(t, meridian, peakAlt, nadirAlt))
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    drawPath(path, color = color, style = Stroke(width = strokeWidth))
}

private fun DrawScope.drawSunMarker(sunX: Float, sunY: Float) {
    drawCircle(CelestialColors.SunGlow.copy(alpha = 0.3f), radius = 42f, center = Offset(sunX, sunY))
    val fm = ChartPaints.sunEmoji.fontMetrics
    val emojiCenterY = sunY - (fm.ascent + fm.descent) / 2f
    drawContext.canvas.nativeCanvas.drawText("☀", sunX, emojiCenterY, ChartPaints.sunEmoji)
}

/**
 * Azimuth-mode rise/set/meridian markers.
 */
private fun DrawScope.drawAzimuthMarkers(
    info: CelestialInfo, horizonY: Float,
    azToX: (Double) -> Float, altToY: (Double) -> Float,
) {
    val sunMidAz = (info.sunRiseAz + info.sunSetAz) / 2.0
    drawMarker(
        azToX(info.sunRiseAz),
        horizonY,
        "R",
        CelestialColors.SunColor.copy(alpha = 0.6f),
        ChartPaints.sunMarker,
        -14f
    )
    drawMarker(
        azToX(info.sunSetAz),
        horizonY,
        "S",
        CelestialColors.SunColor.copy(alpha = 0.6f),
        ChartPaints.sunMarker,
        -14f
    )
    drawMarker(
        azToX(sunMidAz),
        altToY(info.sunPeakAlt),
        "M",
        CelestialColors.SunColor.copy(alpha = 0.4f),
        ChartPaints.sunMeridian,
        -16f
    )

    val moonMidAz = (info.moonRiseAz + info.moonSetAz) / 2.0
    drawMarker(
        azToX(info.moonRiseAz),
        horizonY,
        "R",
        CelestialColors.MoonColor.copy(alpha = 0.5f),
        ChartPaints.moonMarker,
        36f
    )
    drawMarker(
        azToX(info.moonSetAz),
        horizonY,
        "S",
        CelestialColors.MoonColor.copy(alpha = 0.5f),
        ChartPaints.moonMarker,
        36f
    )
    drawMarker(
        azToX(moonMidAz),
        altToY(info.moonPeakAlt),
        "M",
        CelestialColors.MoonColor.copy(alpha = 0.4f),
        ChartPaints.moonMeridian,
        -16f
    )
}

/**
 * Time-mode rise/set/meridian markers.
 */
private fun DrawScope.drawTimeMarkers(
    info: CelestialInfo, horizonY: Float,
    timeToX: (Double) -> Float, altToY: (Double) -> Float,
) {
    drawMarker(
        timeToX(info.sunRiseHour),
        horizonY,
        "R",
        CelestialColors.SunColor.copy(alpha = 0.6f),
        ChartPaints.sunMarker,
        -14f
    )
    drawMarker(
        timeToX(info.sunSetHour),
        horizonY,
        "S",
        CelestialColors.SunColor.copy(alpha = 0.6f),
        ChartPaints.sunMarker,
        -14f
    )
    drawMarker(
        timeToX(info.sunMeridianHour),
        altToY(info.sunPeakAlt),
        "M",
        CelestialColors.SunColor.copy(alpha = 0.4f),
        ChartPaints.sunMeridian,
        -16f
    )
    drawMarker(
        timeToX(info.moonRiseHour),
        horizonY,
        "R",
        CelestialColors.MoonColor.copy(alpha = 0.5f),
        ChartPaints.moonMarker,
        36f
    )
    drawMarker(
        timeToX(info.moonSetHour),
        horizonY,
        "S",
        CelestialColors.MoonColor.copy(alpha = 0.5f),
        ChartPaints.moonMarker,
        36f
    )
    drawMarker(
        timeToX(info.moonMeridianHour),
        altToY(info.moonPeakAlt),
        "M",
        CelestialColors.MoonColor.copy(alpha = 0.4f),
        ChartPaints.moonMeridian,
        -16f
    )
}
