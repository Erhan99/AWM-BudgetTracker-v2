package com.example.budgettracker_v2.repositories.balans

import com.example.budgettracker_v2.models.Balans

data class ApiResponseBalansen (
    val status: String,
    val data: List<Balans>
)