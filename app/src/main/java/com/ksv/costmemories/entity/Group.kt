package com.ksv.costmemories.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [Index("product")]
)
data class Group(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val product: String
)
