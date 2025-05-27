package com.example.budgettracker_v2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.budgettracker_v2.repositories.klant.apiKlant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> get() = _isLoggedIn
    private val _userId = MutableStateFlow<String?>(null)
    val userId = _userId.asStateFlow()

    fun setUserId(id: String) {
        _userId.value = id
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val response = apiKlant.getKlanten()
            val user = response.data.find { it.kl_email == email && it.kl_wachtwoord == password }

            if (user != null) {
                _isLoggedIn.value = true
                _userId.value = user.kl_id.toString()
                onResult(true)
            } else {
                _isLoggedIn.value = false
                onResult(false)
            }
        }
    }
    fun logout() {
        _isLoggedIn.value = false
    }
}