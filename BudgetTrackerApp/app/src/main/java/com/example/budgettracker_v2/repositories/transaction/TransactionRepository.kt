package com.example.budgettracker_v2.repositories.transaction

import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.network.retrofit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiServiceTransaction{
    @GET("transacties/")
    suspend fun getTransacties(): ApiResponseTransactions

    @GET("transacties/")
    suspend fun geTransactiesByUser(
        @Query("balansid") balansid: String
    ): ApiResponseTransactions

    @POST("transacties/")
    suspend fun postTransacties(
        @Body transaction: PostTransactionDto
    ): Response<Unit>

    @PUT("transacties/")
    suspend fun updateTransacties(
        @Body transaction: Transaction
    ): Response<Unit>

    @DELETE("transacties/")
    suspend fun deleteTransacties(
        @Query("id") id: String
    ): Response<Unit>
}

//maakt een class van de interface
val apiTransaction: ApiServiceTransaction by lazy {
    retrofit.create(ApiServiceTransaction::class.java)
}

