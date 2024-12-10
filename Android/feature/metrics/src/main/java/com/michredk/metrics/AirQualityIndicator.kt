package com.michredk.metrics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.michredk.common.design.AQTheme
import kotlin.math.max

@Preview(showBackground = true)
@Composable
fun PreviewCustomComponent() {
    AQTheme {
        UsageOfIndicator()
    }
}

@Composable
fun UsageOfIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonAirQualityIndexIndicator(pm25 = 25, pm100 = 100)
    }
}

@Composable
fun CommonAirQualityIndexIndicator(
    canvasSize: Dp = 200.dp,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    foregroundIndicatorStrokeWidth: Float = 100f,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    valueFontSize: TextUnit = MaterialTheme.typography.displayMedium.fontSize,
    valueSuffix: String = "GB",
    descriptionText: String = "Remaining",
    descriptionColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
    descriptionFontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    pm25: Int,
    pm100: Int
) {
    val qualityLevels = arrayOf("Good", "Moderate", "Poor", "Unhealthy", "Severe")
    val pm25Level = when (pm25){
        in 0..15 -> qualityLevels[0]
        in 16..30 -> qualityLevels[1]
        in 31..55 -> qualityLevels[2]
        in 56..110 -> qualityLevels[3]
        else -> qualityLevels[4]
    }
    val pm100Level = when (pm25){
        in 0..25 -> qualityLevels[0]
        in 26..50 -> qualityLevels[1]
        in 51..75 -> qualityLevels[2]
        in 76..100 -> qualityLevels[3]
        else -> qualityLevels[4]
    }
    val overalQuality = max(qualityLevels.indexOf(pm25Level), qualityLevels.indexOf(pm100Level))
    val indicatorLevel = overalQuality.toFloat() / qualityLevels.size.toFloat()

    // 240 is maximum angle of indicator so 240 / 100 percent = 2.4
    // targetValue must be a maximum of 240 so max 100 percent is allowed
    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * minOf(indicatorLevel, 100f)).toFloat(),
        animationSpec = tween(1000), label = "indicator value animation"
    )
    val valueProgression by animateIntAsState(
        targetValue = pm100,
        animationSpec = tween(1000)
    )
    val animatedValueColor by animateColorAsState(
        targetValue = if (pm100 == 0)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        else
            valueColor,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val indicatorSize = size / 1.25f
                // background
                circularIndicator(
                    sweepAngle = 240f,
                    indicatorSize = indicatorSize,
                    color = backgroundIndicatorColor,
                    strokeWidth = backgroundIndicatorStrokeWidth,
                )
                // animated foreground
                circularIndicator(
                    sweepAngle = sweepAngle,
                    indicatorSize = indicatorSize,
                    color = foregroundIndicatorColor,
                    strokeWidth = foregroundIndicatorStrokeWidth,
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            valueText = valueProgression,
            valueColor = animatedValueColor,
            valueFontSize = valueFontSize,
            valueSuffix = valueSuffix,
            descriptionText = descriptionText,
            descriptionColor = descriptionColor,
            descriptionFontSize = descriptionFontSize
        )
    }
}

/**
 * start and sweep angles:
 * * Calculating the arc angle starts at 3 o'clock and continues clockwise.
 * * Arc is drawn from 'startAngle' to 'startAngle + sweepAngle'.
 */
fun DrawScope.circularIndicator(
    sweepAngle: Float,
    indicatorSize: Size,
    color: Color,
    strokeWidth: Float
) {
    drawArc(
        size = indicatorSize,
        color = color,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Butt
        ),
        // centering indicator on drawing env by calculating point to which
        // we want to offset top-left corner of the arc
        topLeft = Offset(
            x = (size.width - indicatorSize.width) / 2f,
            y = (size.height - indicatorSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    valueText: Int,
    valueColor: Color,
    valueFontSize: TextUnit,
    valueSuffix: String,
    descriptionText: String,
    descriptionColor: Color,
    descriptionFontSize: TextUnit
) {
    Text(
        text = descriptionText,
        color = descriptionColor,
        fontSize = descriptionFontSize,
        textAlign = TextAlign.Center,
    )
    Text(
        text = "$valueText $valueSuffix",
        color = valueColor,
        fontSize = valueFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}