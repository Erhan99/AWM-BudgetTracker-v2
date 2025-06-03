package com.example.budgettracker_v2.viewmodels


import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.budgettracker_v2.models.Balans
import com.example.budgettracker_v2.models.Klant
import com.example.budgettracker_v2.repositories.balans.apiBalans
import com.example.budgettracker_v2.repositories.klant.CreateKlantResponse
import com.example.budgettracker_v2.repositories.klant.apiKlant
import com.example.budgettracker_v2.viewmodels.state.LoginUIState
import com.example.budgettracker_v2.viewmodels.state.TransactionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun register(
        voornaam: String,
        achternaam: String,
        email: String,
        wachtwoord: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val nieuwKlant = Klant(
                kl_id = null,
                kl_naam = achternaam,
                kl_voornaam = voornaam,
                kl_email = email,
                kl_wachtwoord = wachtwoord,
                kl_isAdmin = false,
                bl_id = null
            )
            try {
                val response: Response<CreateKlantResponse> = apiKlant.postKlanten(nieuwKlant)
                if (response.isSuccessful) {
                    val body: CreateKlantResponse? = response.body()
                    if (body != null) {
                        val nieuweKlId: Int = body.bk_code
                        val nieuweBalans = Balans(
                            bl_id = null,
                            bl_inkomsten = null,
                            bl_uitgaven = null,
                            bl_kl_id = nieuweKlId
                        )
                        val balansResponse: Response<Unit> = apiBalans.postBalans(nieuweBalans)
                        if (balansResponse.isSuccessful) {
                            onResult(true)
                        } else {
                            Log.e("Register", "Balans POST mislukte: ${balansResponse.code()}, ${balansResponse.message()}")
                            onResult(false)
                        }
                    } else {
                        Log.e("Register", "Response body klant is null")
                        onResult(false)
                    }
                } else {
                    Log.e("Register", "Klant POST mislukte: ${response.code()}, ${response.message()}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("Register", "Fout tijdens registreren", e)
                onResult(false)
            }
        }
    }
}