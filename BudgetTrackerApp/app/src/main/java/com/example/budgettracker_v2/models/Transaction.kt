package com.example.budgettracker_v2.models

data class Transaction (
    val tr_id: Int? = null,
    val tr_bedrag: Double,
    val tr_mededeling: String,
    val tr_begunstigde: String,
    val tr_dt_id: Int,
    val tr_ct_id: Int,
    val tr_bl_id: Int,
    val ct_naam: String = "",
    val dt_datum: String = "",
    val dt_jaar: Int = 0,
    val dt_maand: String = "",
    val dt_maand_num: Int = 0,
    val dt_dag: Int = 0
)