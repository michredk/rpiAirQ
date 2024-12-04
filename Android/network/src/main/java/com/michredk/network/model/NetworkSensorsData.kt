package com.michredk.network.model

import java.time.LocalDateTime

data class NetworkSensorsData(
    val id: Int,
    val temp: Int,
    val time: LocalDateTime
)
