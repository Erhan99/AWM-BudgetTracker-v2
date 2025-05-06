package com.example.budgettracker_v2.viewmodels.state

import com.example.budgettracker_v2.models.Transaction

data class TransactionUIState (
    val transactions: List<Transaction>? = null,
    val transactiesHuidigeMaand: List<Transaction>? = null,
    val inkomstenHuidigeMaand: Double = 0.0,
    val uitgavenHuidigeMaan: Double = 0.0
)