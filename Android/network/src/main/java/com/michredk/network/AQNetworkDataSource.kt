package com.michredk.network

interface AQNetworkDataSource {
    suspend fun getData(): NetworkResponse<NetworkSensorsData>
}