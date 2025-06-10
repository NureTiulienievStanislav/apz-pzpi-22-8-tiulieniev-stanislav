package com.trackway.app.data

import android.content.Context
import com.trackway.app.data.api.TrackWayApi
import com.trackway.app.data.repository.TrackWayRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val BASE_URL = "https://api.trackway.com/" // Replace with your actual API URL

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    override val api: TrackWayApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackWayApi::class.java)
    }

    override val repository: TrackWayRepository by lazy {
        TrackWayRepository(api, context)
    }
} 