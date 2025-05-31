package com.example.budgettracker_v2.ui

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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