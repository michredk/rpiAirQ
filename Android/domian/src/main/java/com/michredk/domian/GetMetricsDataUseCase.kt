package com.michredk.domian

import com.michredk.data.repository.SensorDataRepository
import com.michredk.metrics.MetricsScreenUiState
import com.michredk.network.NetworkResult
import com.michredk.network.model.NetworkSensorData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMetricsDataUseCase @Inject constructor(
    private val sensorDataRepository: SensorDataRepository,
    private val calculateCAQIUseCase: CalculateCAQIUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(): MetricsScreenUiState = withContext(defaultDispatcher) {
        when (val response = sensorDataRepository.getData()) {
            is NetworkResult.Success -> {
                val sensorData = response.data as NetworkSensorData
                val caqi = calculateCAQIUseCase(sensorData.pm25, sensorData.pm100)
                return@withContext MetricsScreenUiState.Success(
                    sensorData, caqi
                )
            }
            is NetworkResult.Error -> return@withContext MetricsScreenUiState.Error("${response.code}, ${response.message}")
            is NetworkResult.Exception -> return@withContext MetricsScreenUiState.Error("${response.e.message}")
        }
    }
}