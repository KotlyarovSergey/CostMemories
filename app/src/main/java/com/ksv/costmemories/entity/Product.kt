package com.ksv.costmemories.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_titles",
    indices = [Index("title")]
)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String
)
