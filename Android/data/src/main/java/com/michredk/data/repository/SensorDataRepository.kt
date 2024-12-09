package com.michredk.data.repository

import com.michredk.data.util.Syncable
import com.michredk.model.SensorData
import com.michredk.network.NetworkResult
import com.michredk.network.model.NetworkSensorData
import kotlinx.coroutines.flow.Flow

interface SensorDataRepository {

    suspend fun getData(): NetworkResult<NetworkSensorData>

}