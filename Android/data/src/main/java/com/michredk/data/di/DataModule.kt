package com.michredk.data.di

import com.michredk.data.repository.OnlyOnlineSensorDataRepository
import com.michredk.data.repository.SensorDataRepository
import com.michredk.data.util.ConnectivityManagerNetworkMonitor
import com.michredk.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindSensorsDataRepository(
        notesRepository: OnlyOnlineSensorDataRepository
    ): SensorDataRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor
}