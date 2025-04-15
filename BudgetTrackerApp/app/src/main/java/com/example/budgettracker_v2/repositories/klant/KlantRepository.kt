package com.example.budgettracker_v2.repositories.klant

import com.example.budgettracker_v2.models.Klant
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.network.retrofit
import com.example.budgettracker_v2.repositories.transaction.ApiServiceTransaction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiServiceKlant{
    @GET("klanten/")
    suspend fun getKlanten(): ApiResponseKlanten

    @POST("klanten/")
    suspend fun postKlanten(
        @Body klant: Klant
    ): Response<Unit>

    @PUT("klanten/")
    suspend fun updateKlanten(
        @Body klant: Klant
    ): Response<Unit>

    @DELETE("klanten/")
    suspend fun deleteKlanten(
        @Query("id") id: String
    ): Response<Unit>
}

//maakt een class van de interface
val apiKlant: ApiServiceKlant by lazy {
    retrofit.create(ApiServiceKlant::class.java)
}
