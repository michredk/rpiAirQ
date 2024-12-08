package com.michredk.data.repository

import com.michredk.data.model.asLocalData
import com.michredk.model.SensorData
import com.michredk.network.AQNetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OnlyOnlineSensorDataRepository @Inject constructor(
    private val network: AQNetworkDataSource,
) : SensorDataRepository {

    override suspend fun getData(): SensorData {
        return network.getData().data.asLocalData()
    }
}