package com.ksv.costmemories.entity

data class PurchaseTuple(
    val id: Long,
    val date: String,
    val product: String,
    val title: String,
    val shop: String,
    val cost: Int,
    val comment: String = "",
) {
    companion object{
        val EMPTY = PurchaseTuple(0, "",  "", "", "", 0,"")
    }
}