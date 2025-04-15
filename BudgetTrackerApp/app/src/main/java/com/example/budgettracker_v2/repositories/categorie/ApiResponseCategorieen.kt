package com.example.budgettracker_v2.repositories.categorie

import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.models.Klant

data class ApiResponseCategorieen (
    val status: String,
    val data: List<Categorie>
)