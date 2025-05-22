package com.example.budgettracker_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.budgettracker_v2.navigation.AppNavigation
import com.example.budgettracker_v2.ui.BudgetTrackerApp
import com.example.budgettracker_v2.ui.NavBar
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import com.example.compose.BudgetTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTrackerTheme {
                BudgetTrackerApp()
            }
        }
    }
}

@Composable
fun BudgetTrackerApp(loginViewModel: LoginViewModel = viewModel()) {
    val navController = rememberNavController()
    val isLoggedIn = loginViewModel.isLoggedIn.value

    Scaffold(
        bottomBar = {
            if (isLoggedIn) {
                NavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController = navController, loginViewModel = loginViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetTrackerPreview() {
    BudgetTrackerTheme() {
    }
}