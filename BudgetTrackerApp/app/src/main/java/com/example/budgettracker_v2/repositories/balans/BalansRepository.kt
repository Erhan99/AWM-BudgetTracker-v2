package com.example.budgettracker_v2.repositories.balans

import com.example.budgettracker_v2.models.Balans
import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.network.retrofit
import com.example.budgettracker_v2.repositories.klant.ApiResponseKlanten
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiServiceBalans{
    @GET("balansen/")
    suspend fun getBalansen(): ApiServiceBalans

    @POST("balansen/")
    suspend fun postBalans(
        @Body balans: Balans
    ): Response<Unit>

    @PUT("balansen/")
    suspend fun updateBalansen(
        @Body balans: Balans
    ): Response<Unit>

    @DELETE("balansen/")
    suspend fun deleteBalansen(
        @Query("id") id: String
    ): Response<Unit>
}

//maakt een class van de interface
val apiBalans: ApiServiceBalans by lazy {
    retrofit.create(ApiServiceBalans::class.java)
}