package com.michredk.network

import com.michredk.network.model.NetworkSensorData
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import javax.inject.Inject

@Serializable
data class NetworkResponse<T>(
    val data: T,
)

private interface RetrofitAQNetworkApi: AQNetworkDataSource {
    @GET("data")
    override suspend fun getData(): NetworkResponse<NetworkSensorData>
}

private const val AQ_BASE_URL = BuildConfig.BASE_URL

internal class RetrofitAQNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
): AQNetworkDataSource {
    private val networkApi =
        Retrofit.Builder()
            .baseUrl(AQ_BASE_URL)
            // Using a callFactory lambda with dagger.Lazy<Call.Factory> prevents
            // OkHttp from being initialized on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitAQNetworkApi::class.java)

    override suspend fun getData(): NetworkResponse<NetworkSensorData> =
        networkApi.getData()

}