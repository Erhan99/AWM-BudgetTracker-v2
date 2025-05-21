package com.example.budgettracker_v2.repositories.categorie

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

interface ApiServiceCategorie{
    @GET("categorieën/")
    suspend fun getCategorieen(): ApiResponseCategorieen

    @POST("categorieën/")
    suspend fun postCategorieen(
        @Body categorie: Categorie
    ): Response<Unit>

    @PUT("categorieën/")
    suspend fun updateCategorieen(
        @Body categorie: Categorie
    ): Response<Unit>

    @DELETE("categorieën/")
    suspend fun deleteCategorieen(
        @Query("id") id: String
    ): Response<Unit>
}

//maakt een class van de interface
val apiCategory: ApiServiceCategorie by lazy {
    retrofit.create(ApiServiceCategorie::class.java)
}
