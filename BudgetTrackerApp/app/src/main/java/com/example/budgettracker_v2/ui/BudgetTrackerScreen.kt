package com.example.budgettracker_v2.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.budgettracker_v2.navigation.AppNavigation

@Composable
fun BudgetTrackerApp(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { NavBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(navController)
        }
    }
}