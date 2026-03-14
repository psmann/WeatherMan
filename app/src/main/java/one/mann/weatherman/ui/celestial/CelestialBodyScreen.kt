package one.mann.weatherman.ui.celestial

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import one.mann.domain.logic.HALF_LUNAR_CYCLE
import one.mann.weatherman.ui.celestial.chart.drawMoonDisc
import one.mann.weatherman.ui.celestial.chart.drawSky
import one.mann.weatherman.ui.celestial.components.InfoCard
import one.mann.weatherman.ui.celestial.components.InfoRow
import one.mann.weatherman.ui.celestial.theme.CelestialColors
import one.mann.weatherman.ui.common.models.CelestialInfo

/* Created by Psmann. */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CelestialBodyScreenPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                CelestialChart(CelestialInfo(), azimuthMode = true)
                CelestialInfoSection(CelestialInfo())
            }
        }
    }
}

/**
 * Chart-only composable used inside the SunCycle ViewHolder area.
 */
@Composable
internal fun CelestialChart(info: CelestialInfo, azimuthMode: Boolean) {
    val groundColor by animateColorAsState(
        targetValue = if (info.isDaytime) CelestialColors.GroundDay else CelestialColors.GroundNight,
        animationSpec = tween(600), label = "ground",
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawSky(groundColor, info, azimuthMode)
        }
    }
}

/**
 * Info cards section used in the Celestial ViewHolder at the bottom.
 */
@Composable
internal fun CelestialInfoSection(info: CelestialInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        // Sun info card
        InfoCard(titleContent = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("☀\uFE0F", fontSize = 18.sp)
                Text("Sun", color = Color.White, fontFamily = FontFamily.SansSerif, fontSize = 18.sp)
            }
        }) {
            InfoRow("Altitude", info.sunAltitude)
            InfoRow("Azimuth", info.sunAzimuth)
            InfoRow("Rise", info.sunRiseInfo)
            InfoRow("Set", info.sunSetInfo)
            InfoRow("Zenith", info.sunZenithInfo)
            InfoRow("Nadir", info.sunNadirInfo)
        }

        // Moon info card
        InfoCard(titleContent = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Canvas(modifier = Modifier
                    .width(18.dp)
                    .height(18.dp)) {
                    drawMoonDisc(
                        size.width / 2f, size.height / 2f, size.minDimension / 2f,
                        info.moonIlluminationFraction.toFloat(), info.moonAgeDays < HALF_LUNAR_CYCLE,
                    )
                }
                Text("Moon", color = Color.White, fontFamily = FontFamily.SansSerif, fontSize = 18.sp)
            }
        }) {
            InfoRow("Altitude", info.moonAltitude)
            InfoRow("Azimuth", info.moonAzimuth)
            InfoRow("Rise", info.moonRiseInfo)
            InfoRow("Set", info.moonSetInfo)
            InfoRow("Zenith", info.moonZenithInfo)
            InfoRow("Nadir", info.moonNadirInfo)
            InfoRow("Phase", info.moonPhase)
            InfoRow("Age", info.moonAge)
            InfoRow("Illumination", info.moonIllumination)
        }

        Spacer(Modifier.height(24.dp))
    }
}
