package com.example.budgettracker_v2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.ui.HomeScreen
import com.example.budgettracker_v2.ui.InsightScreen
import com.example.budgettracker_v2.ui.TransactionCreateScreen
import com.example.budgettracker_v2.ui.TransactionScreen
import com.example.budgettracker_v2.ui.TransactionDetailsScreen
import com.example.budgettracker_v2.ui.TransactionEditScreen
import com.google.gson.Gson

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable(route = "transactions") { TransactionScreen(navController = navController) }
        composable("insights") { InsightScreen() }
        composable("transactionDetails/{transactionJson}") { backStackEntry ->
            val transactionJson = backStackEntry.arguments?.getString("transactionJson")
            val transaction = Gson().fromJson(transactionJson, Transaction::class.java)
            TransactionDetailsScreen(transaction = transaction, navController = navController)
        }
        composable("transactionEdit") {
            TransactionEditScreen(navController = navController)
        }
        composable("transactionCreate") {
            TransactionCreateScreen(navController = navController)
        }
    }
}