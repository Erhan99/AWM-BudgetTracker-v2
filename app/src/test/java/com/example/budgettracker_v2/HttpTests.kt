package com.example.budgettracker_v2

import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.repositories.apiTransaction
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class HttpTests {

    @Test
    fun getTransactieTest(){
        runBlocking {
            val response = apiTransaction.getTransacties()
            assertEquals("200", response.status)
        }
    }

    @Test
    fun postTransactieTest(){
        runBlocking {
            val transaction = Transaction(tr_bedrag = 999.00, tr_mededeling = "testing post", tr_begunstigde = "testing post", tr_dt_id = 1, tr_bl_id = 1, tr_ct_id = 1)
            val response = apiTransaction.postTransacties(transaction)
            assertEquals(true, response.isSuccessful)
        }
    }
}