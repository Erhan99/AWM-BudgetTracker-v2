package com.example.budgettracker_v2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.budgettracker_v2.R
import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.viewmodels.TransactionViewModel
import com.google.gson.Gson
import kotlin.math.abs
import kotlin.math.exp

@Composable
fun TransactionScreen(navController: NavController, VM: TransactionViewModel = viewModel()) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("transactionCreate") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nieuwe transactie")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
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
                                } else {
                                    isLoading.value = false
                                }
                            }
                        }
                    }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Transactions",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,

                        )
                }
                TransactionList(transactions = items, listState, isLoading.value, navController)
            }
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
                TransactionCard(modifier = Modifier.padding(8.dp), transaction = transaction, navController = navController)
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

@Composable
fun TransactionCard(modifier: Modifier = Modifier, transaction: Transaction, navController: NavController){
    val date = String.format("%s %s %s", transaction.dt_dag, transaction.dt_maand.substring(0,3), transaction.dt_jaar)
    val price = String.format("€ %s", transaction.tr_bedrag)
    val expanded = remember { mutableStateOf(false) }
    Card (modifier = modifier){
        Column (modifier = Modifier.padding(16.dp).fillMaxWidth()){
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Column (
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = transaction.tr_begunstigde,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,

                    )
                    Text(
                        modifier = Modifier.padding(vertical = 3.dp),
                        text = transaction.ct_naam ,
                        fontSize = 12.sp
                    )
                }
                Column {
                    Text(
                        text = price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(transaction.tr_bedrag < 0) Color(0xFFF33535) else Color(0xFF2E7D32)
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 3.dp),
                        text = date,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Right
                    )
                }
                TransactionDropDownMenu(expanded, transaction, navController)

            }
        }
    }
}


@Composable
fun TransactionDropDownMenu(expanded: MutableState<Boolean>, transaction: Transaction, navController: NavController){
    Box{
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