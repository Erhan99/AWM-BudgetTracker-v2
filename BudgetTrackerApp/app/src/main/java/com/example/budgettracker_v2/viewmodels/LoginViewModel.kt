package com.example.budgettracker_v2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.budgettracker_v2.repositories.klant.apiKlant
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> get() = _isLoggedIn

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val response = apiKlant.getKlanten()
            val isValidUser = response.data.any {
                it.kl_email == email && it.kl_wachtwoord == password
            }
            _isLoggedIn.value = isValidUser
            onResult(isValidUser)
        }
    }
}