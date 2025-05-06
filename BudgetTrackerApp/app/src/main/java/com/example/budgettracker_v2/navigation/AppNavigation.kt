package com.example.budgettracker_v2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.budgettracker_v2.ui.HomeScreen
import com.example.budgettracker_v2.ui.InsightScreen
import com.example.budgettracker_v2.ui.TransactionScreen
import com.example.budgettracker_v2.ui.TransactionDetailsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable(route = "transactions") { TransactionScreen(navController = navController) }
        composable("insights") { InsightScreen() }
        composable("transactionDetails/{transactionId}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            TransactionDetailsScreen(transactionId = transactionId ?: "", navController = navController)
        }
    }
}