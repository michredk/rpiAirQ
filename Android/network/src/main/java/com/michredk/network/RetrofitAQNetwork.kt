package com.michredk.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable
data class NetworkResponse<T>(
    val data: T,
)

private interface RetrofitClaNetworkApi: AQNetworkDataSource {

    @GET("data")
    override suspend fun getData(): NetworkResponse<NetworkSensorsData>
}

private const val AQ_BASE_URL = BuildConfig.BASE_URL

class RetrofitAQNetwork {
}