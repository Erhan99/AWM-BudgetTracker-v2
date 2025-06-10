package com.example.budgettracker_v2.ui

import android.util.Log
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
import com.example.budgettracker_v2.viewmodels.LoginViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    transaction: Transaction,
    navController: NavController,
    loginVM: LoginViewModel = viewModel(),
    transactionVM: TransactionViewModel = viewModel()
) {
    val loginState by loginVM.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }

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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                    modifier = Modifier.weight(1f),
                    enabled = !isDeleting
                ) {
                    Text("Bewerken")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                    enabled = !isDeleting
                ) {
                    if (isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onError
                        )
                    } else {
                        Text("Verwijderen")
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Transactie verwijderen") },
            text = { Text("Weet je zeker dat je deze transactie wilt verwijderen? Deze actie kan niet ongedaan worden gemaakt.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        coroutineScope.launch {
                            isDeleting = true
                            try {
                                val response = apiTransaction.deleteTransacties(transaction.tr_id.toString())
                                if (response.isSuccessful) {
                                    snackbarHostState.showSnackbar("Transactie succesvol verwijderd")
                                    transactionVM.getTransactions(loginState.userId.toString())
                                    navController.navigate("transactions") {
                                        popUpTo("transactions") { inclusive = true }
                                    }
                                } else {
                                    snackbarHostState.showSnackbar("Fout bij verwijderen van transactie")
                                }
                            } catch (e: Exception) {
                                Log.e("TransactionDetails", "Error deleting transaction", e)
                                snackbarHostState.showSnackbar("Er is een fout opgetreden")
                            } finally {
                                isDeleting = false
                            }
                        }
                    }
                ) {
                    Text("Verwijderen", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuleren")
                }
            }
        )
    }
}