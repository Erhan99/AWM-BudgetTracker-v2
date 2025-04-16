package com.example.budgettracker_v2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.budgettracker_v2.ui.HomeScreen
import com.example.budgettracker_v2.ui.InsightScreen
import com.example.budgettracker_v2.ui.TransactionScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("transactions") { TransactionScreen() }
        composable("insights") { InsightScreen() }
    }
}