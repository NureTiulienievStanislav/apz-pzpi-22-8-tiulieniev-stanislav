package com.trackway.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trackway.app.data.AppContainer
import com.trackway.app.ui.screens.*
import com.trackway.app.ui.theme.TrackWayTheme
import com.trackway.app.ui.viewmodels.AuthViewModel
import com.trackway.app.ui.viewmodels.VehicleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as TrackWayApp).container

        setContent {
            TrackWayTheme {
                TrackWayApp(appContainer)
            }
        }
    }
}

@Composable
fun TrackWayApp(appContainer: AppContainer) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    var isAdmin by remember { mutableStateOf(false) }

    val authViewModel: AuthViewModel = viewModel { AuthViewModel(appContainer.repository) }
    val vehicleViewModel: VehicleViewModel = viewModel { VehicleViewModel(appContainer.repository) }

    when (currentScreen) {
        Screen.Login -> {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { currentScreen = Screen.Register },
                onLoginSuccess = { currentScreen = Screen.Dashboard }
            )
        }
        Screen.Register -> {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { currentScreen = Screen.Login },
                onRegisterSuccess = { currentScreen = Screen.Dashboard }
            )
        }
        Screen.Dashboard -> {
            DashboardScreen(
                viewModel = vehicleViewModel,
                isAdmin = isAdmin,
                onAddVehicle = { currentScreen = Screen.AddVehicle },
                onLogout = {
                    authViewModel.logout()
                    currentScreen = Screen.Login
                }
            )
        }
        Screen.AddVehicle -> {
            AddVehicleScreen(
                viewModel = vehicleViewModel,
                onNavigateBack = { currentScreen = Screen.Dashboard }
            )
        }
    }
}

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Dashboard : Screen()
    object AddVehicle : Screen()
} 