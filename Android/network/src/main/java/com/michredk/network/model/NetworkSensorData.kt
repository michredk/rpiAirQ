package com.michredk.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkSensorData(
    val temperature: Float,
    val humidity: Float,
    val pm10: Int,
    val pm25: Int,
    val pm100: Int
)
