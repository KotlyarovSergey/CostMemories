package com.ksv.costmemories.entity

import androidx.room.ColumnInfo

data class PurchaseTuple(
    val id: Long,
    val date: String,
    val product: String,
    val title: String,
    @ColumnInfo(name = "shop_name") val shop: String,
    val cost: Int,
    val comment: String = "",
) {
    override fun toString(): String {
        return if(comment == null){
            "$id: $date: $product $title $shop ${cost}р."
        } else{
            "$id: $date: $product $title $shop ${cost}р. ($comment)"
        }
    }
    companion object{
        val EMPTY = PurchaseTuple(0, "",  "", "", "", 0,"")
    }
}