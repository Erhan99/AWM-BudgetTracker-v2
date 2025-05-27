package com.example.budgettracker_v2.viewmodels.state

import com.example.budgettracker_v2.models.Transaction

data class TransactionUIState (
    val transactions: List<Transaction>? = null
)