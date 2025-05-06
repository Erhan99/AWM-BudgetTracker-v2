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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(transactionId: String, navController: NavController, VM: TransactionViewModel = viewModel()) {
    val uiState by VM.uiState.collectAsState()
    val transaction = uiState.transactions?.find { it.tr_id.toString() == transactionId }

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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            transaction?.let {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Begunstigde: ${it.tr_begunstigde}", style = MaterialTheme.typography.headlineMedium)
                    Text(text = "Bedrag: € ${it.tr_bedrag}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Categorie: ${it.ct_naam}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Datum: ${it.dt_dag}-${it.dt_maand}-${it.dt_jaar}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Mededeling: ${it.tr_mededeling}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(onClick = { }, modifier = Modifier.weight(1f)) {
                            Text("Bewerken")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                            Text("Verwijderen")
                        }
                    }
                }
            } ?: Text(text = "Transactie niet gevonden", style = MaterialTheme.typography.headlineSmall)
        }
    }
}