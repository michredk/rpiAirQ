package com.michredk.network

import com.michredk.network.model.NetworkSensorsData

interface AQNetworkDataSource {
    suspend fun getData(): NetworkResponse<NetworkSensorsData>
}