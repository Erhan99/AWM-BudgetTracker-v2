package com.example.budgettracker_v2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budgettracker_v2.models.Transaction
import androidx.lifecycle.viewModelScope
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
import kotlinx.coroutines.launch
import android.util.Log
import com.example.budgettracker_v2.viewmodels.state.TransactionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class TransactionViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(TransactionUIState())
    val uiState: StateFlow<TransactionUIState> = _uiState.asStateFlow()

     fun getTransactions(user:String){
        viewModelScope.launch {
            try{
                val result = apiTransaction.geTransactiesByUser(user)
                Log.d("api result", result.toString())


                val transactionsMaand = result.data.filter{ t ->
                    t.dt_maand_num == LocalDate.now().monthValue && t.dt_jaar == LocalDate.now().year
                }

                val inkomsten = result.data.filter{ t ->
                    t.tr_bedrag > 0.0 && t.dt_maand_num == LocalDate.now().monthValue && t.dt_jaar == LocalDate.now().year
                }.sumOf { it.tr_bedrag }
                Log.d("inkomsten", inkomsten.toString())

                val uitgaven = result.data.filter{ t ->
                    t.tr_bedrag < 0.0 && t.dt_maand_num == LocalDate.now().monthValue && t.dt_jaar == LocalDate.now().year
                }.sumOf { it.tr_bedrag }

                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = result.data,
                        transactiesHuidigeMaand = transactionsMaand,
                        inkomstenHuidigeMaand = inkomsten,
                        uitgavenHuidigeMaan = uitgaven
                    )
                }
            }
            catch (e: Exception){
                Log.d("api","api related problem: " + e)
            }

        }
    }

    //functie niet compleet, gemaakt om te testen
     fun postTransaction(){
        viewModelScope.launch {
            try{
                val test = Transaction(tr_bedrag = 999.00, tr_mededeling = "testing post", tr_begunstigde = "testing post", tr_dt_id = 1, tr_bl_id = 1, tr_ct_id = 1)
                val result = apiTransaction.postTransacties(test)
                Log.d("api result", result.toString())
            }
            catch (e: Exception){
                Log.d("api","api post related problem: " + e)
            }

        }
    }
}