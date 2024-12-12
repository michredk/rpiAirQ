package com.michredk.metrics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michredk.common.design.AQTheme
import com.michredk.common.design.VeryGoodQualityColor
import com.michredk.common.design.GoodQualityColor
import com.michredk.common.design.ModerateQualityColor
import com.michredk.common.design.UnhealthyQualityColor
import com.michredk.common.design.PoorQualityColor

@Composable
fun HorizontalAirQualityIndicator(
    qualityLevel: QualityLevel,
    modifier: Modifier = Modifier
) {
    val qualityLevelsStringArray = remember {arrayOf("Very Good", "Good", "Moderate", "Poor", "Unhealthy")}
    val qualityLevelsIntArray = remember {arrayOf(0, 25, 50, 75, 100, 125)}
    val qualityColorsArray = remember {
        arrayOf(
            VeryGoodQualityColor,
            GoodQualityColor,
            ModerateQualityColor,
            PoorQualityColor,
            UnhealthyQualityColor
        )
    }
    val segmentsCount = remember {qualityColorsArray.size}
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    var columnWidth by remember { mutableIntStateOf(0) }
    var segmentWidth by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .onSizeChanged { size ->
            columnWidth = size.width
            segmentWidth = with(density) { (size.width - 2 * 30.dp.toPx()) / segmentsCount }.toInt()
        }
        .drawBehind {
            // Draw indicator bar with quality level names
            drawCircle(
                color = qualityColorsArray[0],
                radius = 15f,
                center = Offset(30.dp.toPx(), 40.dp.toPx())
            )
            for (i in 0..<segmentsCount) {
                drawLine(
                    color = qualityColorsArray[i],
                    strokeWidth = 30f,
                    start = Offset(30.dp.toPx() + i * segmentWidth, 40.dp.toPx()),
                    end = Offset(30.dp.toPx() + (i + 1) * segmentWidth, 40.dp.toPx()),
                )
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(qualityLevelsStringArray[i]),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        30.dp.toPx() + (i * segmentWidth) + (segmentWidth - textLayoutResult.size.width) / 2,
                        20.dp.toPx()
                    )
                )
            }
            drawCircle(
                color = qualityColorsArray[segmentsCount - 1],
                radius = 15f,
                center = Offset(columnWidth - 30.dp.toPx(), 40.dp.toPx())
            )
            // Draw triangle
            val path = Path().apply {
                moveTo(
                    30.dp.toPx() + segmentWidth / 2 + qualityLevel.ordinal * segmentWidth,
                    15.dp.toPx()
                ) // center
                lineTo(
                    30.dp.toPx() + segmentWidth / 2 - 4.dp.toPx() + qualityLevel.ordinal * segmentWidth,
                    8.dp.toPx()
                ) // left
                lineTo(
                    30.dp.toPx() + segmentWidth / 2 + 4.dp.toPx() + qualityLevel.ordinal * segmentWidth,
                    8.dp.toPx()
                ) // right
                close()
            }
            drawPath(
                path = path,
                color = qualityColorsArray[qualityLevel.ordinal]
            )
            // Draw quality levels boundaries
            for (i in qualityLevelsIntArray.indices) {
                var text = qualityLevelsIntArray[i].toString()
                if (i == qualityLevelsIntArray.size-1) text += "+"
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(text),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                )
                val xOffset = (i * segmentWidth).toFloat() + segmentWidth/3f
                val finalXOffset = when(i) {
                    0 -> xOffset + 10f
                    in 1..qualityLevelsIntArray.size-2 -> xOffset
                    else -> xOffset - 10f
                }
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        finalXOffset,
                        50.dp.toPx()
                    )
                )
            }
        }) {}

}

@Preview(showBackground = true)
@Composable
fun PreviewHorizontalIndicator() {
    AQTheme {
        UsageOfHorizontalIndicator()
    }
}

@Composable
fun UsageOfHorizontalIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalAirQualityIndicator(qualityLevel = QualityLevel.ModerateQuality)
    }
}
