package com.michredk.airq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michredk.data.repository.SensorDataRepository
import com.michredk.domian.GetMetricsDataUseCase
import com.michredk.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.michredk.metrics.MetricsScreenUiState.Loading
import com.michredk.metrics.MetricsScreenUiState.Success
import com.michredk.metrics.MetricsScreenUiState.Error
import com.michredk.metrics.MetricsScreenUiState
import com.michredk.network.model.NetworkSensorData

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getMetricsDataUseCase: GetMetricsDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MetricsScreenUiState>(Loading)
    val uiState: StateFlow<MetricsScreenUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
                _uiState.value = getMetricsDataUseCase()
        }
    }

}
