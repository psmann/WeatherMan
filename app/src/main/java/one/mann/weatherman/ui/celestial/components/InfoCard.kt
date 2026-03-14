package one.mann.weatherman.ui.celestial.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import one.mann.weatherman.ui.celestial.theme.CelestialColors

/* Created by Psmann. */

@Composable
internal fun InfoCard(
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        color = CelestialColors.CardBg,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when {
                titleContent != null -> titleContent()
                title != null -> Text(title, color = Color.White, fontFamily = FontFamily.SansSerif, fontSize = 14.sp)
            }
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
internal fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, color = Color.White, fontFamily = FontFamily.SansSerif, fontSize = 14.sp)
        Text(value, color = Color.White, fontFamily = FontFamily.SansSerif, fontSize = 14.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun InfoCardPreview() {
    InfoCard(title = "☀\uFE0F Sun") {
        InfoRow("Altitude", "42.50°")
        InfoRow("Azimuth", "180.00° S")
        InfoRow("Rise", "6:30 AM  (-0.83°, 90.00° E)")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun InfoRowPreview() {
    Column(modifier = Modifier.background(CelestialColors.CardBg).padding(16.dp)) {
        InfoRow("Phase", "Waxing Gibbous")
        InfoRow("Age", "10.5 days")
        InfoRow("Illumination", "75.0%")
    }
}
