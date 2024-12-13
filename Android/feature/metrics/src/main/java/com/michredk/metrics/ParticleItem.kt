package com.michredk.metrics

import android.health.connect.datatypes.units.Percentage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michredk.common.design.AQTheme
import com.michredk.common.design.GoodQualityColor
import com.michredk.common.design.ModerateQualityColor
import com.michredk.common.design.PoorQualityColor
import com.michredk.common.design.Typography
import com.michredk.common.design.UnhealthyQualityColor
import com.michredk.common.design.VeryGoodQualityColor

@Composable
fun ParticleItem(particleName: String, particleValue: Int, concentration: Float, width: Dp) {
    val qualityColor = when(concentration) {
            in 0f..0.25f -> VeryGoodQualityColor
            in 0.26f..0.50f -> GoodQualityColor
            in 0.51f..0.75f -> ModerateQualityColor
            in 0.76f..0.99f -> PoorQualityColor
            else -> UnhealthyQualityColor
    }
    Column(modifier = Modifier.width(width)) {
        Text(
            particleName,
            color = Color.DarkGray,
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
        Row {
            Text(
                modifier = Modifier.padding(end = 2.dp),
                text = particleValue.toString(),
                fontSize = Typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.DarkGray,
                            fontSize = 10.sp,
                            baselineShift = BaselineShift(0.2f)
                        )
                    ) {
                        append("μg/m")
                    }

                    // Apply font size and baseline shift for the superscript
                    withStyle(
                        style = SpanStyle(
                            color = Color.DarkGray,
                            fontSize = 9.sp,
                            baselineShift = BaselineShift(0.4f)
                        )
                    ) {
                        append("3")
                    }
                }
            )
        }
        Column(modifier = Modifier
            .width(100.dp)
            .height(20.dp)
            .drawBehind {
                // Draw indicator bar with quality level names
                drawCircle(
                    color = qualityColor,
                    radius = 15f,
                    center = Offset(5.dp.toPx(), 10.dp.toPx())
                )
                drawLine(
                    color = Color.LightGray,
                    strokeWidth = 30f,
                    start = Offset(5.dp.toPx(), 10.dp.toPx()),
                    end = Offset(width.toPx() - 5.dp.toPx(), 10.dp.toPx()),
                )
                drawLine(
                    color = qualityColor,
                    strokeWidth = 30f,
                    start = Offset(5.dp.toPx(), 10.dp.toPx()),
                    end = Offset(5.dp.toPx() + concentration * (width.toPx() - 10.dp.toPx()), 10.dp.toPx()),
                )
                drawCircle(
                    color = if (concentration == 1f) qualityColor else Color.LightGray,
                    radius = 15f,
                    center = Offset(width.toPx() - 5.dp.toPx(), 10.dp.toPx())
                )

            }){}
    }
}

@Preview
@Composable
private fun SingleParticleDataDisplayPreview() {
    AQTheme {
        ParticleItem("PM 2.5", 3, 0.03f, width = 200.dp)
    }
}

@Preview
@Composable
private fun SingleParticleUnhealthyDataDisplayPreview() {
    AQTheme {
        ParticleItem("PM 10", 100, 1f, width = 150.dp)
    }
}