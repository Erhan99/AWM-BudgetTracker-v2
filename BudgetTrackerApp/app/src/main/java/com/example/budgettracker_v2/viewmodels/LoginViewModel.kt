package com.example.budgettracker_v2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.budgettracker_v2.repositories.klant.apiKlant
import com.example.budgettracker_v2.viewmodels.state.LoginUIState
import com.example.budgettracker_v2.viewmodels.state.TransactionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val response = apiKlant.getKlanten()
            val user = response.data.find { it.kl_email == email && it.kl_wachtwoord == password }
            if (user != null) {
                _uiState.update {
                    currentState ->
                    currentState.copy(
                        isLoggedIn = true,
                        userId = user.kl_id,
                        balansId = user.bl_id
                    )
                }
                onResult(true)
            } else {
                _uiState.update {
                        currentState ->
                    currentState.copy(
                        isLoggedIn = false,
                        userId = -1
                    )
                }
                onResult(false)
            }
        }
    }
    fun logout() {
        _uiState.update {
                currentState ->
            currentState.copy(
                isLoggedIn = false,
                userId = -1
            )
        }
    }
}