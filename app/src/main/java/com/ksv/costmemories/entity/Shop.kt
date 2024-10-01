package com.ksv.costmemories.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "shops",
    indices = [Index(value = ["name"], unique = true)])
data class Shop(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val shop_name: String
) {
    override fun toString(): String {
        return "$id: $shop_name"
    }
}
