package com.trackway.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.trackway.app.data.models.Vehicle
import com.trackway.app.ui.viewmodels.VehicleViewModel
import com.trackway.app.ui.viewmodels.VehiclesState

@Composable
fun DashboardScreen(
    viewModel: VehicleViewModel,
    isAdmin: Boolean,
    onAddVehicle: () -> Unit,
    onLogout: () -> Unit
) {
    val vehiclesState by viewModel.vehiclesState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TrackWay Dashboard") },
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = onAddVehicle) {
                            Icon(Icons.Default.Add, contentDescription = "Add Vehicle")
                        }
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (vehiclesState) {
                is VehiclesState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is VehiclesState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items((vehiclesState as VehiclesState.Success).vehicles) { vehicle ->
                            VehicleCard(
                                vehicle = vehicle,
                                isAdmin = isAdmin,
                                onDelete = { viewModel.deleteVehicle(vehicle.id) }
                            )
                        }
                    }
                }
                is VehiclesState.Error -> {
                    Text(
                        text = (vehiclesState as VehiclesState.Error).message,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun VehicleCard(
    vehicle: Vehicle,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${vehicle.make} ${vehicle.model}",
                    style = MaterialTheme.typography.h6
                )
                if (isAdmin) {
                    TextButton(onClick = onDelete) {
                        Text("Delete", color = MaterialTheme.colors.error)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Year: ${vehicle.year}")
            Text("VIN: ${vehicle.vin}")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Front IoT: ${String.format("%.1f", vehicle.frontIot)}m")
                Text("Back IoT: ${String.format("%.1f", vehicle.backIot)}m")
            }
        }
    }
} 