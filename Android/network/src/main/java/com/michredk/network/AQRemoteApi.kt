package com.michredk.network

import com.michredk.network.model.NetworkSensorData
import retrofit2.Response

interface AQRemoteApi {
    suspend fun getData(): Response<NetworkSensorData>
}