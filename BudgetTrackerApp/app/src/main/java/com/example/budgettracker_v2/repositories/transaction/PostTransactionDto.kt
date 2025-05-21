package com.example.budgettracker_v2.repositories.transaction

data class PostTransactionDto (
    val tr_bedrag: Double,
    val tr_mededeling: String,
    val tr_begunstigde: String,
    val tr_dt_id: Int,
    val tr_ct_id: Int,
    val tr_bl_id: Int,
)