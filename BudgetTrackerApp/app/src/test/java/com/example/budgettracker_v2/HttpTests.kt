package com.example.budgettracker_v2

import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
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
    fun getTransactiesByUser(){
        runBlocking {
            val response = apiTransaction.geTransactiesByUser("2")
            assertEquals(true, response.isSuccessful)
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

    @Test
    fun updateTransactieTest(){
        runBlocking {
            val transaction = Transaction(tr_id = 8 ,tr_bedrag = 999.00, tr_mededeling = "testing post", tr_begunstigde = "testing post", tr_dt_id = 1, tr_bl_id = 1, tr_ct_id = 1)
            val response = apiTransaction.updateTransacties(transaction)
            assertEquals(true, response.isSuccessful)
        }
    }

    @Test
    fun updateTransactieZonderIdTest(){
        runBlocking {
            val transaction = Transaction(tr_bedrag = 999.00, tr_mededeling = "testing post", tr_begunstigde = "testing post", tr_dt_id = 1, tr_bl_id = 1, tr_ct_id = 1)
            val response = apiTransaction.updateTransacties(transaction)
            assertEquals(false, response.isSuccessful)
        }
    }

    @Test
    fun Deletetransactietest(){
        runBlocking {
            val response = apiTransaction.deleteTransacties("24")
            assertEquals(true, response.isSuccessful)
        }
    }
}