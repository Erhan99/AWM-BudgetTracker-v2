package com.example.budgettracker_v2.repositories

import com.example.budgettracker_v2.models.Transaction

// nut van de klasse = bij get request krijgen we een object ipv een lijst met transacties
data class ApiResponseTransactions(
    val status: String,
    val data: List<Transaction>
)
