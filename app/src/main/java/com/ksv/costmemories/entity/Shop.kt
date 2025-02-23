package com.ksv.costmemories.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shops",
    indices = [Index("shop_name")]
)
data class Shop(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shop_name: String
) {
    override fun toString(): String {
        return "$id: $shop_name"
    }
}
