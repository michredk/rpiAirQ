package com.michredk.data.repository

import com.michredk.data.util.Syncable
import com.michredk.model.SensorData
import kotlinx.coroutines.flow.Flow

interface SensorDataRepository: Syncable {

    suspend fun getData(): SensorData

}