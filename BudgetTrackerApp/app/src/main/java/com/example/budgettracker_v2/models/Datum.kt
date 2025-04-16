package com.example.budgettracker_v2.models

import java.time.LocalDate

data class Datum (
    val dt_id: Int? = null,
    val dt_datum: String,
    val dt_jaar: Int,
    val dt_maand: String,
    val dt_maand_num: Int,
    val dt_dag: Int
)