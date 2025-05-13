package com.example.budgettracker_v2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.google.gson.Gson
import kotlin.math.exp

@Composable
fun TransactionScreen(navController: NavController, VM: TransactionViewModel = viewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val uiState by VM.uiState.collectAsState()
        val listState = rememberLazyListState()
        val items = remember { mutableStateListOf<Transaction>() }
        val currentOffset = remember { mutableStateOf(0) }
        val isLoading = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            VM.getTransactions("4")
        }

        LaunchedEffect(uiState.transactions) {
            uiState.transactions?.let {
                if (items.isEmpty()) {
                    val initialChunk = it.take(5)
                    items.addAll(initialChunk)
                    currentOffset.value = initialChunk.size
                }
            }
        }

        LaunchedEffect(Unit) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleIndex ->
                    if (lastVisibleIndex == items.size - 1) {
                        uiState.transactions?.let { allTransactions ->
                            val nextChunk = allTransactions
                                .drop(currentOffset.value)
                                .take(5)
                            if (nextChunk.isNotEmpty()) {
                                items.addAll(nextChunk)
                                currentOffset.value += nextChunk.size
                                isLoading.value = true
                            }
                            else{
                                isLoading.value = false
                            }
                        }
                    }
                }
        }
        TransactionList(transactions = items, listState, isLoading.value, navController)
    }
}

@Composable
fun TransactionCard(modifier: Modifier = Modifier, transaction: Transaction, navController: NavController){
    val date = String.format("%s %s %s", transaction.dt_dag, transaction.dt_maand, transaction.dt_jaar)
    val price = String.format("€ %s", transaction.tr_bedrag)
    val expanded = remember { mutableStateOf(false) }
    Card (modifier = modifier){
        Column (modifier = Modifier.padding(16.dp)){
            Row {
                Text(
                    text = transaction.tr_begunstigde,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = price,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if(transaction.tr_bedrag < 0) Color(0xFFC62828) else Color(0xFF2E7D32)
                )
                Box {
                    IconButton(onClick = { expanded.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                expanded.value = false
                                val transactionJson = Gson().toJson(transaction)
                                navController.navigate("transactionDetails/$transactionJson")
                            },
                            text = { Text("Details") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                expanded.value = false
                                navController.navigate("transactionEdit")
                            },
                            text = { Text("Bewerken") }
                        )
                        DropdownMenuItem(
                            onClick = { expanded.value = false },
                            text = { Text("Verwijderen") }
                        )
                    }
                }
            }

                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = transaction.ct_naam ,
                    fontSize = 12.sp,
                )
                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = date,
                    fontSize = 12.sp,
                )
                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = transaction.tr_mededeling,
                    fontSize = 12.sp,
                )

        }
    }
}

@Composable
fun TransactionList(transactions: List<Transaction>?, state: LazyListState, isLoading: Boolean, navController: NavController){
    if (transactions != null){
        LazyColumn (
            state = state
        ){
            items(transactions){
                    transaction ->
                TransactionCard(modifier = Modifier.padding(6.dp), transaction = transaction, navController = navController)
            }
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
    else{
        Text(
            text = "no transactions"
        )
    }
}