package com.michredk.data.repository

import com.michredk.network.NetworkResult
import com.michredk.network.model.NetworkSensorData

interface SensorDataRepository {

    suspend fun getData(): NetworkResult<NetworkSensorData>

}