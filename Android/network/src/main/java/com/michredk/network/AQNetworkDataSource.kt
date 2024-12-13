package com.michredk.network

import com.michredk.network.model.NetworkSensorData

interface AQNetworkDataSource {
    suspend fun getData(): NetworkResult<NetworkSensorData>
}