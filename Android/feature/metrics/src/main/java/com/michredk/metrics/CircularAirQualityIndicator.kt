package com.michredk.metrics

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.michredk.common.design.AQTheme
import com.michredk.common.design.VeryGoodQualityColor

@Preview(showBackground = true)
@Composable
fun PreviewCircularIndicator() {
    AQTheme {
        UsageOfCircularIndicator()
    }
}

@Composable
fun UsageOfCircularIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularAirQualityIndicator(caqiValue = 15, qualityLevelColor = VeryGoodQualityColor)
    }
}

@Composable
fun CircularAirQualityIndicator(
    canvasSize: Dp = 200.dp,
    indicatorStrokeWidth: Float = 30f,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    valueFontSize: TextUnit = MaterialTheme.typography.displaySmall.fontSize,
    caqiValue: Int,
    qualityLevelColor: Color
) {
    // 240 is maximum angle of indicator so 240 / 100 percent = 2.4
    // targetValue must be a maximum of 240 so max 100 percent is allowed
    val sweepAngle by animateFloatAsState(
        targetValue = caqiValue.toFloat() * 360 / 100,
        animationSpec = tween(1000), label = "indicator value animation"
    )
    val valueProgression by animateIntAsState(
        targetValue = caqiValue,
        animationSpec = tween(1000)
    )
    val animatedValueColor by animateColorAsState(
        targetValue = if (caqiValue == 100)
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
                val innerIndicatorSize = size / 1.75f
                val innerCircleSize = size / 2.75f
                circularIndicator(
                    sweepAngle = sweepAngle,
                    indicatorSize = indicatorSize,
                    foregroundColor = qualityLevelColor,
                    strokeWidth = indicatorStrokeWidth,
                )
                val foregroundBrush = createStripeBrush(
                    stripeColor = qualityLevelColor,
                    stripeWidth = 1.dp,
                    stripeToGapRatio = 1.8f
                )
                val backgroundBrush = createStripeBrush(
                    stripeColor = qualityLevelColor.copy(0.15f),
                    stripeWidth = 1.dp,
                    stripeToGapRatio = 1.8f
                )
                innerCircularIndicator(
                    foregroundBrush = foregroundBrush,
                    backgroundBrush = backgroundBrush,
                    sweepAngle = sweepAngle,
                    indicatorSize = innerIndicatorSize,
                    strokeWidth = indicatorStrokeWidth
                )
                drawArc(
                    size = innerCircleSize,
                    color = Color.Gray.copy(0.15f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(
                        width = 5f
                    ),
                    topLeft = Offset(
                        x = (size.width - innerCircleSize.width) / 2f,
                        y = (size.height - innerCircleSize.height) / 2f
                    )
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            valueText = valueProgression,
            valueColor = animatedValueColor,
            valueFontSize = valueFontSize
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
    foregroundColor: Color,
    backgroundColor: Color = foregroundColor.copy(alpha = 0.15f),
    strokeWidth: Float
) {
    drawArc(
        size = indicatorSize,
        color = foregroundColor,
        startAngle = 270f,
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
    drawArc(
        size = indicatorSize,
        color = backgroundColor,
        startAngle = 270f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Butt
        ),
        topLeft = Offset(
            x = (size.width - indicatorSize.width) / 2f,
            y = (size.height - indicatorSize.height) / 2f
        )
    )
}

fun DrawScope.innerCircularIndicator(
    foregroundBrush: Brush,
    backgroundBrush: Brush,
    sweepAngle: Float,
    indicatorSize: Size,
    strokeWidth: Float
) {
    drawArc(
        size = indicatorSize,
        brush = foregroundBrush,
        startAngle = 270f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Butt
        ),
        topLeft = Offset(
            x = (size.width - indicatorSize.width) / 2f,
            y = (size.height - indicatorSize.height) / 2f
        )
    )
    drawArc(
        size = indicatorSize,
        brush = backgroundBrush,
        startAngle = 270f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Butt
        ),
        topLeft = Offset(
            x = (size.width - indicatorSize.width) / 2f,
            y = (size.height - indicatorSize.height) / 2f
        )
    )
}

private fun Density.createStripeBrush(
    stripeColor: Color,
    stripeWidth: Dp,
    stripeToGapRatio: Float
): Brush {
    val stripeWidthPx = stripeWidth.toPx()
    val stripeGapWidthPx = stripeWidthPx / stripeToGapRatio
    val brushSizePx = stripeGapWidthPx + stripeWidthPx
    val stripeStart = stripeGapWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated
    )
}

@Composable
fun EmbeddedElements(
    valueText: Int,
    valueColor: Color,
    valueFontSize: TextUnit
) {
    Text(
        text = "$valueText",
        color = valueColor,
        fontSize = valueFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}