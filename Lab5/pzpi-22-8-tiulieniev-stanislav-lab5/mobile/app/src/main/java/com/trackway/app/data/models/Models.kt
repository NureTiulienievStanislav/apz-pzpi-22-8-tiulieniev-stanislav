package com.trackway.app.data.models

data class User(
    val username: String,
    val role: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class Vehicle(
    val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val vin: String,
    var frontIot: Double = 0.0,
    var backIot: Double = 0.0
)

data class AddVehicleRequest(
    val make: String,
    val model: String,
    val year: Int,
    val vin: String
) 