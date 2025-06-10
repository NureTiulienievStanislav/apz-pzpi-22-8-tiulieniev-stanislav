package com.trackway.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackway.app.data.models.Vehicle
import com.trackway.app.data.repository.TrackWayRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehicleViewModel(private val repository: TrackWayRepository) : ViewModel() {
    private val _vehiclesState = MutableStateFlow<VehiclesState>(VehiclesState.Loading)
    val vehiclesState: StateFlow<VehiclesState> = _vehiclesState

    private var iotUpdateJob: Job? = null

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        viewModelScope.launch {
            _vehiclesState.value = VehiclesState.Loading
            repository.getVehicles()
                .onSuccess { vehicles ->
                    _vehiclesState.value = VehiclesState.Success(vehicles)
                    startIotUpdates(vehicles)
                }
                .onFailure { error ->
                    _vehiclesState.value = VehiclesState.Error(error.message ?: "Failed to load vehicles")
                }
        }
    }

    fun addVehicle(make: String, model: String, year: Int, vin: String) {
        viewModelScope.launch {
            repository.addVehicle(make, model, year, vin)
                .onSuccess { vehicle ->
                    val currentVehicles = (_vehiclesState.value as? VehiclesState.Success)?.vehicles ?: emptyList()
                    _vehiclesState.value = VehiclesState.Success(currentVehicles + vehicle)
                }
                .onFailure { error ->
                    _vehiclesState.value = VehiclesState.Error(error.message ?: "Failed to add vehicle")
                }
        }
    }

    fun deleteVehicle(id: String) {
        viewModelScope.launch {
            repository.deleteVehicle(id)
                .onSuccess {
                    val currentVehicles = (_vehiclesState.value as? VehiclesState.Success)?.vehicles ?: emptyList()
                    _vehiclesState.value = VehiclesState.Success(currentVehicles.filter { it.id != id })
                }
                .onFailure { error ->
                    _vehiclesState.value = VehiclesState.Error(error.message ?: "Failed to delete vehicle")
                }
        }
    }

    private fun startIotUpdates(vehicles: List<Vehicle>) {
        iotUpdateJob?.cancel()
        iotUpdateJob = viewModelScope.launch {
            while (true) {
                delay(5000) // Update every 5 seconds
                val updatedVehicles = vehicles.map { vehicle ->
                    val (frontIot, backIot) = repository.generateRandomIotData()
                    vehicle.copy(frontIot = frontIot, backIot = backIot)
                }
                _vehiclesState.value = VehiclesState.Success(updatedVehicles)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        iotUpdateJob?.cancel()
    }
}

sealed class VehiclesState {
    object Loading : VehiclesState()
    data class Success(val vehicles: List<Vehicle>) : VehiclesState()
    data class Error(val message: String) : VehiclesState()
} 