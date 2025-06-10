package com.example.budgettracker_v2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.budgettracker_v2.models.Transaction
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(transaction: Transaction, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactiedetails") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Terug")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = transaction.tr_begunstigde,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "€ ${transaction.tr_bedrag}",
                style = MaterialTheme.typography.titleLarge,
                color = if (transaction.tr_bedrag < 0) Color(0xFFC62828) else Color(0xFF2E7D32)
            )

            Text(text = "Categorie: ${transaction.ct_naam}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Datum: ${transaction.dt_dag}-${transaction.dt_maand}-${transaction.dt_jaar}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Mededeling: ${transaction.tr_mededeling}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        val transactionJson = Gson().toJson(transaction)
                        navController.navigate("transactionEdit/$transactionJson")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Bewerken")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Verwijderen")
                }
            }
        }
    }
}