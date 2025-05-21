package com.example.budgettracker_v2.repositories.datum

import com.google.gson.annotations.SerializedName

data class DatumPostResponseDto (
    @SerializedName("dt_id") val dt_id: Int?
)