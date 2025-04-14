package com.example.budgettracker_v2.repositories

import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.network.retrofit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiServiceTransaction{
    //getest => werkt
    @GET("transacties/")
    suspend fun getTransacties(): ApiResponseTransactions

    //getest => werkt
    @POST("transacties/")
    suspend fun postTransacties(
        @Body transaction: Transaction
    ): Response<Unit>

    // niet getest
    @PUT("transacties/")
    suspend fun updateTransacties(
        @Body transaction: Transaction
    ): Response<Unit>

    // niet getest
    @DELETE("transacties/")
    suspend fun deleteTransacties(
        @Body transaction: Transaction
    ): Response<Unit>
}

//maakt een class van de interface
val apiTransaction: ApiServiceTransaction by lazy {
    retrofit.create(ApiServiceTransaction::class.java)
}

