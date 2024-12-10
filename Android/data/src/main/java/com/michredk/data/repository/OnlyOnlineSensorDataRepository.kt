package com.michredk.data.repository

import com.michredk.network.AQNetworkDataSource
import com.michredk.network.NetworkResult
import com.michredk.network.model.NetworkSensorData
import javax.inject.Inject

internal class OnlyOnlineSensorDataRepository @Inject constructor(
    private val network: AQNetworkDataSource,
) : SensorDataRepository {

    override suspend fun getData(): NetworkResult<NetworkSensorData> {
        return network.getData()
    }
}