package com.example.budgettracker_v2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budgettracker_v2.models.Transaction
import androidx.lifecycle.viewModelScope
import com.example.budgettracker_v2.repositories.apiTransaction
import kotlinx.coroutines.launch
import java.io.IOException
import android.util.Log
class TransactionViewModel : ViewModel(){
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    init {
        //getTransactions()
        postTransaction()
    }

    private fun getTransactions(){
        viewModelScope.launch {
            try{
                val result = apiTransaction.getTransacties()
                Log.d("api result", result.toString())
                _transactions.value = result.data
            }
            catch (e: Exception){
                Log.d("api","api related problem: " + e)
            }

        }
    }

    //functie niet compleet, gemaakt om te testen
    public fun postTransaction(){
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