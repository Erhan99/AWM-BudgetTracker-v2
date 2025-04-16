package com.example.budgettracker_v2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.viewmodels.TransactionViewModel

@Composable
fun TransactionScreen(VM: TransactionViewModel = viewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(Unit) {
            VM.getTransactions("4") // This only runs once when the composable is first composed
        }
        val uiState by VM.uiState.collectAsState()
        TransactionList(transactions = uiState.transactions)
    }
}

@Composable
fun TransactionCard(modifier: Modifier = Modifier, transaction: Transaction){
    val date = String.format("%s %s %s", transaction.dt_dag, transaction.dt_maand, transaction.dt_jaar)
    Card (modifier = modifier){
        Column (modifier = Modifier.padding(16.dp)){
            Row {
                Text(
                    text = transaction.tr_begunstigde,
                    fontSize = 18.sp
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = transaction.tr_bedrag.toString(),
                    fontSize = 18.sp
                )
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
fun TransactionList(transactions: List<Transaction>?){
    if (transactions != null){
        LazyColumn {
            items(transactions){
                    transaction ->
                TransactionCard(transaction = transaction)
            }
        }
    }
    else{
        Text(
            text = "no transactions"
        )
    }
}

@Preview
@Composable
fun prev(){
    val VM = TransactionViewModel()
    VM.getTransactions("3")
    val uiState by VM.uiState.collectAsState()
    TransactionList(transactions = uiState.transactions)
}