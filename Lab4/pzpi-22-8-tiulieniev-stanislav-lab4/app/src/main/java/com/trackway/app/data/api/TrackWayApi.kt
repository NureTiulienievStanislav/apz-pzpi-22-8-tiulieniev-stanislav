package com.trackway.app.data.api

import com.trackway.app.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface TrackWayApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("vehicles")
    suspend fun getVehicles(): Response<List<Vehicle>>

    @POST("vehicles")
    suspend fun addVehicle(@Body request: AddVehicleRequest): Response<Vehicle>

    @DELETE("vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") id: String): Response<Unit>
} 