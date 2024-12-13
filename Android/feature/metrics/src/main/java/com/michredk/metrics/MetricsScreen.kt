package com.michredk.metrics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michredk.common.design.AQTheme
import com.michredk.common.design.VeryGoodQualityColor
import com.michredk.common.design.GoodQualityColor
import com.michredk.common.design.ModerateQualityColor
import com.michredk.common.design.UnhealthyQualityColor
import com.michredk.common.design.PoorQualityColor
import com.michredk.network.model.NetworkSensorData
import java.time.LocalDateTime
import com.michredk.metrics.QualityLevel.VeryGoodQuality
import com.michredk.metrics.QualityLevel.GoodQuality
import com.michredk.metrics.QualityLevel.ModerateQuality
import com.michredk.metrics.QualityLevel.PoorQuality
import com.michredk.metrics.QualityLevel.UnhealthyQuality
import java.time.Duration

sealed interface MetricsScreenUiState {
    data object Loading : MetricsScreenUiState
    data class Error(val message: String) : MetricsScreenUiState
    data class Success(
        val sensorData: NetworkSensorData,
        val caiq: Int,
        val dateTime: LocalDateTime = LocalDateTime.now()
    ) : MetricsScreenUiState
}

enum class QualityLevel {
    VeryGoodQuality,
    GoodQuality,
    ModerateQuality,
    PoorQuality,
    UnhealthyQuality
}

@Composable
fun MetricsScreen(uiState: MetricsScreenUiState.Success) {
    val qualityLevelsStringArray = arrayOf("Very Good", "Good", "Moderate", "Poor", "Unhealthy")
    val qualityColorsArray = arrayOf(
        VeryGoodQualityColor,
        GoodQualityColor,
        ModerateQualityColor,
        PoorQualityColor,
        UnhealthyQualityColor
    )
    val qualityLevel = when (uiState.caiq) {
        in 0..25 -> VeryGoodQuality
        in 26..50 -> GoodQuality
        in 51..75 -> ModerateQuality
        in 76..99 -> PoorQuality
        else -> UnhealthyQuality
    }
    val qualityLevelColor = qualityColorsArray[qualityLevel.ordinal]
    val qualityLevelString = qualityLevelsStringArray[qualityLevel.ordinal]

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TemperatureIndicator(modifier = Modifier.padding(12.dp), uiState.sensorData.temperature)
            CircularAirQualityIndicator(
                caqiValue = uiState.caiq,
                qualityLevelColor = qualityLevelColor
            )
            HumidityIndicator(modifier = Modifier.padding(12.dp), uiState.sensorData.humidity)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "$qualityLevelString Air Quality",
            color = qualityLevelColor,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        LastUpdatedText(uiState.dateTime)
        HorizontalAirQualityIndicator(qualityLevel = qualityLevel)
        AirQualityLevelDescriptionTextBox(
            modifier = Modifier.padding(16.dp),
            qualityLevel = qualityLevel,
            qualityColor = qualityLevelColor
        )
        var columnWidth by remember { mutableStateOf(0.dp) }
        val itemWidth = columnWidth - columnWidth / 6
        val density = LocalDensity.current
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size -> with(density) { columnWidth = size.width.toDp() } }
        ) {
            item {
                ParticleItem(
                    particleName = "PM 1",
                    particleValue = uiState.sensorData.pm10,
                    concentration = when (uiState.sensorData.pm10) {
                        in 0..109 -> uiState.sensorData.pm10 / 110f
                        else -> 1f
                    },
                    qualityColor = when (uiState.sensorData.pm10) {
                        in 0..10 -> VeryGoodQualityColor
                        in 11..20 -> GoodQualityColor
                        in 21..35 -> ModerateQualityColor
                        in 36..60 -> PoorQualityColor
                        else -> UnhealthyQualityColor
                    },
                    width = itemWidth
                )
            }
            item {
                ParticleItem(
                    particleName = "PM 2.5",
                    particleValue = uiState.sensorData.pm25,
                    concentration = when (uiState.sensorData.pm25) {
                        in 0..109 -> uiState.sensorData.pm25 / 110f
                        else -> 1f
                    },
                    qualityColor = when (uiState.sensorData.pm25) {
                        in 0..15 -> VeryGoodQualityColor
                        in 16..30 -> GoodQualityColor
                        in 31..55 -> ModerateQualityColor
                        in 56..110 -> PoorQualityColor
                        else -> UnhealthyQualityColor
                    },
                    width = itemWidth
                )
            }
            item {
                ParticleItem(
                    particleName = "PM 10",
                    particleValue = uiState.sensorData.pm100,
                    concentration = when (uiState.sensorData.pm100) {
                        in 0..179 -> uiState.sensorData.pm100 / 180f
                        else -> 1f
                    },
                    qualityColor = when (uiState.sensorData.pm100) {
                        in 0..25 -> VeryGoodQualityColor
                        in 26..50 -> GoodQualityColor
                        in 51..90 -> ModerateQualityColor
                        in 90..180 -> PoorQualityColor
                        else -> UnhealthyQualityColor
                    },
                    width = itemWidth
                )
            }
        }
    }
}

@Composable
private fun LastUpdatedText(dateTime: LocalDateTime, modifier: Modifier = Modifier) {
    val lastUpdated =
        when (val minutes = Duration.between(dateTime, LocalDateTime.now()).toMinutes()) {
            0L -> "now"
            in 1..59 -> "$minutes mins ago"
            in 60L..Long.MAX_VALUE -> "more than hour ago"
            else -> "error"
        }
    Text(
        modifier = modifier.fillMaxWidth(),
        text = "Last updated: $lastUpdated",
        color = Color.Gray,
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TemperatureIndicator(modifier: Modifier = Modifier, temperature: Float) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Thermostat,
            tint = Color.Black,
            contentDescription = "Thermometer Icon"
        )
        Text(text = "$temperature\u2103")
    }
}

@Composable
fun HumidityIndicator(modifier: Modifier = Modifier, humidity: Float) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Outlined.WaterDrop,
            tint = Color.Black,
            contentDescription = "Thermometer Icon"
        )
        Text(text = "$humidity%")
    }
}

@Preview
@Composable
fun PreviewMetricScreen() {
    AQTheme {
        Scaffold(
            modifier = Modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                MetricsScreen(
                    MetricsScreenUiState.Success(
                        sensorData = NetworkSensorData(
                            temperature = 23.1f,
                            humidity = 47.5f,
                            pm10 = 29,
                            pm25 = 54,
                            pm100 = 45
                        ),
                        caiq = 73,
                        dateTime = LocalDateTime.now()
                    )
                )
            }
        }
    }
}