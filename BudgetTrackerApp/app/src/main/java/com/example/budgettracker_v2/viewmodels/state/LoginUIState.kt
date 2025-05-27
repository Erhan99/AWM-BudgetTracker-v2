package com.example.budgettracker_v2.viewmodels.state

data class LoginUIState (
    val isLoggedIn: Boolean = false,
    val userId: Int? = -1,
    val balansId: Int? = -1
)