package com.example.budgettracker_v2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
import com.example.budgettracker_v2.viewmodels.state.TransactionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransactionViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(TransactionUIState())
    val uiState: StateFlow<TransactionUIState> = _uiState.asStateFlow()

     fun getTransactions(user:String){
        viewModelScope.launch {
            try{
                val result = apiTransaction.geTransactiesByUser(user)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = result.data.sortedByDescending { t ->  LocalDate.parse(t.dt_datum, formatter) }
                    )
                }
            }
            catch (e: Exception){
                Log.d("api","api related problem: " + e)
            }

        }
    }

    fun filterTransactions(
        ct_id: Int? = null,
        begunstigde: String? = null,
        periode: String? = null,
        minBedrag: Double? = null,
        maxBedrag: Double? = null
    ) {
        viewModelScope.launch {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val today = LocalDate.now()

                val periodStart = when (periode) {
                    "Week" -> today.minusWeeks(1)
                    "Maand" -> today.minusMonths(1)
                    "Jaar" -> today.minusYears(1)
                    else -> null
                }

                _uiState.update { currentState ->
                    val filtered = currentState.transactions?.filter { t ->
                        val ctMatch = ct_id == null || t.tr_ct_id == ct_id
                        val begunstigdeMatch = begunstigde.isNullOrBlank() || t.tr_begunstigde.contains(begunstigde, ignoreCase = true)
                        val transactionDate = LocalDate.parse(t.dt_datum, formatter)
                        val dateMatch = periodStart == null || !transactionDate.isBefore(periodStart)

                        val bedrag = t.tr_bedrag
                        val minMatch = minBedrag == null || bedrag >= minBedrag
                        val maxMatch = maxBedrag == null || bedrag <= maxBedrag

                        ctMatch && begunstigdeMatch && dateMatch && minMatch && maxMatch
                    }?.sortedByDescending { t ->
                        LocalDate.parse(t.dt_datum, formatter)
                    }

                    currentState.copy(transactions = filtered)
                }
            } catch (e: Exception) {
                Log.d("api", "api related problem: $e")
            }
        }
    }
}