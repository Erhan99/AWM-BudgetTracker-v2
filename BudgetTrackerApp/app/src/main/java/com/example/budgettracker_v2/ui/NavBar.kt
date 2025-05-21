package com.example.budgettracker_v2.ui

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun NavBar(navController: NavController) {
    val currentRoute = navController.currentDestination?.route
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Transactions") },
            label = { Text("Transacties") },
            selected = currentRoute == "transactions",
            onClick = { navController.navigate("transactions") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = "Insights") },
            label = { Text("Inzichten") },
            selected = currentRoute == "insights",
            onClick = { navController.navigate("insights") }
        )
    }
}