package one.mann.weatherman.ui.celestial.chart

import android.graphics.Paint
import android.graphics.Typeface

/* Created by Psmann. */

internal object ChartConfig {
    const val LEFT_PAD = 80f
    const val RIGHT_PAD = 24f
    const val TOP_PAD = 24f
    const val BOTTOM_PAD = 60f
    const val ALT_MIN = -90.0
    const val ALT_MAX = 90.0
    const val ALT_RANGE = ALT_MAX - ALT_MIN
    const val CURVE_STEPS = 240
    const val STAR_COUNT = 60
}

internal object ChartPaints {

    val horizonLabel: Paint = Paint().apply {
        color = android.graphics.Color.argb(120, 102, 187, 106)
        textSize = 44f; textAlign = Paint.Align.RIGHT; typeface = Typeface.SANS_SERIF
    }
    val altGuide: Paint = Paint().apply {
        color = android.graphics.Color.argb(80, 255, 255, 255)
        textSize = 40f; textAlign = Paint.Align.RIGHT; typeface = Typeface.SANS_SERIF
    }
    val timeLabel: Paint = Paint().apply {
        color = android.graphics.Color.argb(160, 255, 255, 255)
        textSize = 44f; textAlign = Paint.Align.CENTER; typeface = Typeface.SANS_SERIF
    }
    val graphTitle: Paint = Paint().apply {
        color = android.graphics.Color.argb(160, 255, 255, 255)
        textSize = 40f; textAlign = Paint.Align.CENTER; typeface = Typeface.SANS_SERIF
    }
    val sunEmoji: Paint = Paint().apply {
        textSize = 56f; textAlign = Paint.Align.CENTER
    }

    private val markerBase: Paint = Paint().apply {
        textSize = 36f; textAlign = Paint.Align.CENTER; typeface = Typeface.SANS_SERIF
    }
    val sunMarker: Paint = Paint(markerBase).apply {
        color = android.graphics.Color.argb(160, 255, 214, 0)
    }
    val sunMeridian: Paint = Paint(markerBase).apply {
        color = android.graphics.Color.argb(140, 255, 214, 0)
    }
    val moonMarker: Paint = Paint(markerBase).apply {
        color = android.graphics.Color.argb(140, 224, 224, 224)
    }
    val moonMeridian: Paint = Paint(markerBase).apply {
        color = android.graphics.Color.argb(120, 224, 224, 224)
    }

    val sunLegend: Paint = Paint().apply {
        color = android.graphics.Color.argb(200, 255, 214, 0)
        textSize = 44f; typeface = Typeface.SANS_SERIF
    }
    val moonLegend: Paint = Paint().apply {
        color = android.graphics.Color.argb(180, 224, 224, 224)
        textSize = 44f; typeface = Typeface.SANS_SERIF
    }

    val starRandom: java.util.Random = java.util.Random(42L)
}
