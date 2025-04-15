package com.example.budgettracker_v2.repositories.klant

import com.example.budgettracker_v2.models.Klant


data class ApiResponseKlanten(
    val status: String,
    val data: List<Klant>
)

