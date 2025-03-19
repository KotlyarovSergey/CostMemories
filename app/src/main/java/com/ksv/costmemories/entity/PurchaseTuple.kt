package com.ksv.costmemories.entity

data class PurchaseTuple(
    val id: Long,
    val milliseconds: Long,
    val product: String,
    val title: String,
    val shop: String,
    val cost: Int,
    val comment: String = "",
)