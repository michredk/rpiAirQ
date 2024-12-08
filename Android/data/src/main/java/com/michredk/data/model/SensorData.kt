package com.michredk.data.model

import com.michredk.data.repository.SensorDataRepository
import com.michredk.model.SensorData
import com.michredk.network.model.NetworkSensorData

fun NetworkSensorData.asLocalData() =
    SensorData(
        temperature, humidity, pm10, pm25, pm100
    )
