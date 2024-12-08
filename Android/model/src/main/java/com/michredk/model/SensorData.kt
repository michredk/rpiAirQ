package com.michredk.model

import java.time.LocalDateTime

data class SensorData(
    val temperature: Float,
    val humidity: Float,
    val pm10: Int,
    val pm25: Int,
    val pm100: Int,
    val time: LocalDateTime = LocalDateTime.now()
)