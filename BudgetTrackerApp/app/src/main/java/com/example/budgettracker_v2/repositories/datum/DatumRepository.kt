package com.example.budgettracker_v2.repositories.datum

import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.models.Datum
import com.example.budgettracker_v2.network.retrofit
import com.example.budgettracker_v2.repositories.klant.ApiResponseKlanten
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiServiceDatum{
    @GET("datums/")
    suspend fun getDatums(): ApiServiceDatum

    @POST("datums/")
    suspend fun postDatums(
        @Body datum: Datum
    ): Response<Unit>

    @PUT("datums/")
    suspend fun updateDatums(
        @Body datum: Datum
    ): Response<Unit>

    @DELETE("datums/")
    suspend fun deleteDatums(
        @Query("id") id: String
    ): Response<Unit>
}

//maakt een class van de interface
val apiDatum: ApiServiceDatum by lazy {
    retrofit.create(ApiServiceDatum::class.java)
}
