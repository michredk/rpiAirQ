package com.michredk.airq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michredk.data.repository.SensorDataRepository
import com.michredk.model.SensorData
import com.michredk.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.michredk.airq.MainActivityUiState.Loading
import com.michredk.airq.MainActivityUiState.Success
import com.michredk.airq.MainActivityUiState.Error
import com.michredk.network.model.NetworkSensorData

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sensorDataRepository: SensorDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState>(Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            when (val response = sensorDataRepository.getData()) {
                is NetworkResult.Success -> _uiState.emit(Success(response.data as NetworkSensorData))
                is NetworkResult.Error -> _uiState.emit(Error("${response.code}, ${response.message}"))
                is NetworkResult.Exception -> _uiState.emit(Error("${response.e.message}"))
            }
        }
    }

}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Error(val message: String) : MainActivityUiState
    data class Success(val sensorData: NetworkSensorData) : MainActivityUiState
}
