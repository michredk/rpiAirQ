package com.michredk.network

import com.michredk.network.model.NetworkSensorData
import retrofit2.Response

interface AQNetworkDataSource {
    suspend fun getData(): NetworkResult<NetworkSensorData>

}