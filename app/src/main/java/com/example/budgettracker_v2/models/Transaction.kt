package com.example.budgettracker_v2.models

data class Transaction (
    val tr_id: Int? = null,
    val tr_bedrag: Double,
    val tr_mededeling: String,
    val tr_begunstigde: String,
    val tr_dt_id: Int,
    val tr_ct_id: Int,
    val tr_bl_id: Int
)