package com.trackway.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackway.app.data.models.AuthResponse
import com.trackway.app.data.repository.TrackWayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: TrackWayRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.login(username, password)
                .onSuccess { response ->
                    _authState.value = AuthState.Success(response)
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Login failed")
                }
        }
    }

    fun register(username: String, password: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.register(username, password, role)
                .onSuccess { response ->
                    _authState.value = AuthState.Success(response)
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Registration failed")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Initial
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
} 