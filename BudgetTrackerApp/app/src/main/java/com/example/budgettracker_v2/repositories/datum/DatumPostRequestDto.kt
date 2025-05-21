package com.example.budgettracker_v2.repositories.datum

data class DatumPostRequestDto (
    val dt_datum: String,
    val dt_jaar: Int,
    val dt_maand: String,
    val dt_maand_num: Int,
    val dt_dag: Int
)