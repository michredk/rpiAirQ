package com.michredk.network

import com.michredk.network.model.NetworkSensorData
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import javax.inject.Inject

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: Any?) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}

private interface RetrofitAQNetworkApi : AQRemoteApi {
    @GET("data")
    override suspend fun getData(): Response<NetworkSensorData>
}

private const val AQ_BASE_URL = BuildConfig.BASE_URL

internal class RetrofitAQNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : AQNetworkDataSource {
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

    private suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): NetworkResult<T> {
        return try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body)
            } else {
                NetworkResult.Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            NetworkResult.Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }

    override suspend fun getData(): NetworkResult<NetworkSensorData> = handleApi {  networkApi.getData() }

}