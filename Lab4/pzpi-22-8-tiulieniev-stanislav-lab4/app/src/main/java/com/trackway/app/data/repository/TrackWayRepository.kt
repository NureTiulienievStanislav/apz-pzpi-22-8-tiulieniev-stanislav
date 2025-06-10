package com.trackway.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.trackway.app.data.api.TrackWayApi
import com.trackway.app.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.random.Random

private val Context.dataStore by preferencesDataStore(name = "settings")

class TrackWayRepository(
    private val api: TrackWayApi,
    private val context: Context
) {
    private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    private val USER_ROLE_KEY = stringPreferencesKey("user_role")

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    saveToken(authResponse.token)
                    saveUserRole(authResponse.user.role)
                    Result.success(authResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String, role: String): Result<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(username, password, role))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    saveToken(authResponse.token)
                    saveUserRole(authResponse.user.role)
                    Result.success(authResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVehicles(): Result<List<Vehicle>> {
        return try {
            val response = api.getVehicles()
            if (response.isSuccessful) {
                response.body()?.let { vehicles ->
                    Result.success(vehicles)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get vehicles: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addVehicle(make: String, model: String, year: Int, vin: String): Result<Vehicle> {
        return try {
            val response = api.addVehicle(AddVehicleRequest(make, model, year, vin))
            if (response.isSuccessful) {
                response.body()?.let { vehicle ->
                    Result.success(vehicle)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to add vehicle: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteVehicle(id: String): Result<Unit> {
        return try {
            val response = api.deleteVehicle(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete vehicle: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getUserRole(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROLE_KEY]
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USER_ROLE_KEY)
        }
    }

    private suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    private suspend fun saveUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role
        }
    }

    fun generateRandomIotData(): Pair<Double, Double> {
        return Pair(
            Random.nextDouble(0.1, 10.0),
            Random.nextDouble(0.1, 10.0)
        )
    }
} 