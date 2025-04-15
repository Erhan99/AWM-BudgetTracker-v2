package com.example.budgettracker_v2.repositories.datum

import com.example.budgettracker_v2.models.Datum

data class ApiResponseDatums (
    val status: String,
    val data: List<Datum>
)