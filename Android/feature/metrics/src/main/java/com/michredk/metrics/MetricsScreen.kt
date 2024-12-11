package com.michredk.metrics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michredk.common.design.AQTheme
import com.michredk.network.model.NetworkSensorData
import java.time.LocalDateTime

sealed interface MetricsScreenUiState {
    data object Loading : MetricsScreenUiState
    data class Error(val message: String) : MetricsScreenUiState
    data class Success(
        val sensorData: NetworkSensorData,
        val caiq: Int,
        val dateTime: LocalDateTime = LocalDateTime.now()
    ) : MetricsScreenUiState
}

@Composable
fun MetricsScreen(uiState: MetricsScreenUiState.Success) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TemperatureIndicator(modifier = Modifier.padding(12.dp), uiState.sensorData.temperature)
        AirQualityIndexIndicator(caqiValue = uiState.caiq)
        HumidityIndicator(modifier = Modifier.padding(12.dp), uiState.sensorData.humidity)
    }
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
                            pm10 = 10,
                            pm25 = 25,
                            pm100 = 100
                        ),
                        caiq = 100,
                        dateTime = LocalDateTime.now()
                    )
                )
            }
        }
    }
}