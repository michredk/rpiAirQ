package com.michredk.metrics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michredk.common.design.AQTheme
import com.michredk.common.design.GoodQualityColor
import com.michredk.common.design.ModerateQualityColor
import com.michredk.common.design.PoorQualityColor
import com.michredk.common.design.UnhealthyQualityColor
import com.michredk.common.design.VeryGoodQualityColor

@Composable
fun AirQualityLevelDescriptionTextBox(
    qualityLevel: QualityLevel,
    qualityColor: Color,
    modifier: Modifier = Modifier
) {
    val title = when (qualityLevel.ordinal) {
        0 -> "Air is very clean and healthy."
        1 -> "Air is good but has minimal pollutants."
        2 -> "Air is moderate and may cause mild issues."
        3 -> "Air is poor and could affect health."
        else -> "Air is unhealthy and poses risks."
    }
    val desc = when (qualityLevel.ordinal) {
        0 -> "The air quality is excellent, providing a safe and comfortable environment for everyone."
        1 -> "Most people will not experience any adverse effects from the air in this environment."
        2 -> "Sensitive individuals, such as children or those with respiratory issues, might experience slight discomfort."
        3 -> "Long-term exposure to this air quality might pose health risks for vulnerable groups."
        else -> "Exposure to this air quality should be limited, especially for children, elderly people, and those with pre-existing health conditions."
    }
    val icon = when (qualityLevel.ordinal) {
        in 0..1 -> Icons.Default.Check
        else -> Icons.Default.Warning
    }
    var iconWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Card(
        modifier = modifier.wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = qualityColor.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Icon(
                    modifier = Modifier
                        .onSizeChanged { size ->
                            iconWidth = with(density) { size.width.toDp() }
                        }
                        .padding(end = 8.dp),
                    imageVector = icon,
                    tint = Color.Black,
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(lineHeight = 25.sp)
                )
            }
            Row {
                Spacer(
                    modifier = Modifier
                        .width(iconWidth)
                        .padding(end = 8.dp)
                )
                Text(
                    desc,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    style = TextStyle(lineHeight = 25.sp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun VeryGoodQualityDescTextBoxPreview() {
    AQTheme {
        AirQualityLevelDescriptionTextBox(QualityLevel.VeryGoodQuality, VeryGoodQualityColor)
    }
}

@Preview
@Composable
private fun GoodQualityDescTextBoxPreview() {
    AQTheme {
        AirQualityLevelDescriptionTextBox(QualityLevel.GoodQuality, GoodQualityColor)
    }
}

@Preview
@Composable
private fun ModerateQualityDescTextBoxPreview() {
    AQTheme {
        AirQualityLevelDescriptionTextBox(QualityLevel.ModerateQuality, ModerateQualityColor)
    }
}

@Preview
@Composable
private fun PoorQualityDescTextBoxPreview() {
    AQTheme {
        AirQualityLevelDescriptionTextBox(QualityLevel.PoorQuality, PoorQualityColor)
    }
}

@Preview
@Composable
private fun UnhealthyQualityDescTextBoxPreview() {
    AQTheme {
        AirQualityLevelDescriptionTextBox(QualityLevel.UnhealthyQuality, UnhealthyQualityColor)
    }
}