package com.ksv.costmemories.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "product_id")
    val productId: Int,

    @ColumnInfo(name = "shop_id")
    val shopId: Int,

    @ColumnInfo(name = "cost")
    val cost: Int,

    @ColumnInfo(name = "comment")
    val comment: String? = null
)
