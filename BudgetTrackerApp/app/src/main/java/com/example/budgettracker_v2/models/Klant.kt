package com.example.budgettracker_v2.models

data class Klant (
    val kl_id: Int? = null,
    val kl_naam: String,
    val kl_voornaam: String,
    val kl_email: String,
    val kl_wachtwoord: String,
    val kl_isAdmin: Boolean,
    val bl_id: Int?
)