package com.michredk.network.di

import android.content.Context
import com.michredk.network.AQNetworkDataSource
import com.michredk.network.BuildConfig
import com.michredk.network.RetrofitAQNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()

    @Provides
    @Singleton
    fun provideAQNetworkDataSource(
        networkJson: Json,
        okhttpCallFactory: dagger.Lazy<Call.Factory>
    ): AQNetworkDataSource {
        return RetrofitAQNetwork(networkJson, okhttpCallFactory)
    }

}
