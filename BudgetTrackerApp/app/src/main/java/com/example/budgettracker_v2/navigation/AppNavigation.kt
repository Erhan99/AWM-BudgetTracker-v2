package com.example.budgettracker_v2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.ui.HomeScreen
import com.example.budgettracker_v2.ui.InsightScreen
import com.example.budgettracker_v2.ui.LoginScreen
import com.example.budgettracker_v2.ui.RegisterScreen
import com.example.budgettracker_v2.ui.TransactionCreateScreen
import com.example.budgettracker_v2.ui.TransactionScreen
import com.example.budgettracker_v2.ui.TransactionDetailsScreen
import com.example.budgettracker_v2.ui.TransactionEditScreen
import com.example.budgettracker_v2.ui.TransactionFilterScreen
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import com.example.budgettracker_v2.viewmodels.RegisterViewModel
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.google.gson.Gson

@Composable
fun AppNavigation(navController: NavHostController, loginViewModel: LoginViewModel = viewModel(), registerViewModel: RegisterViewModel = viewModel()) {
    val loginState by loginViewModel.uiState.collectAsState()
    val TransactionVM: TransactionViewModel = viewModel()
    NavHost(navController, startDestination = if (loginState.isLoggedIn) "home" else "login") {
        composable("login") { LoginScreen(navController, loginViewModel) }

        composable("register") { RegisterScreen(navController, registerViewModel) }

        composable("home") { HomeScreen(navController, loginVM = loginViewModel) }

        composable(route = "transactions") { TransactionScreen(navController = navController, loginVM = loginViewModel, VM = TransactionVM) }

        composable("insights") { InsightScreen(loginViewModel = loginViewModel) }

        composable("transactionDetails/{transactionJson}") { backStackEntry ->
            val transactionJson = backStackEntry.arguments?.getString("transactionJson")
            val transaction = Gson().fromJson(transactionJson, Transaction::class.java)
            TransactionDetailsScreen(transaction = transaction, navController = navController)
        }

        composable("transactionEdit/{transactionJson}") { backStackEntry ->
            val transactionJson = backStackEntry.arguments?.getString("transactionJson")
            val transaction = Gson().fromJson(transactionJson, Transaction::class.java)
            TransactionEditScreen(
                navController = navController,
                transaction = transaction,
                loginVM = loginViewModel,
                transactionVM = TransactionVM
            )
        }

        composable("transactionCreate") {
            TransactionCreateScreen(navController = navController, loginVM = loginViewModel, VM = TransactionVM)
        }
        composable("transactionFilter") {
            TransactionFilterScreen(navController = navController, loginVM = loginViewModel, VM = TransactionVM)
        }
    }
}
