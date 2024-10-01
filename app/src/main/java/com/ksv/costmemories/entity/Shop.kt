package com.ksv.costmemories.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "shops")
data class Shop(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "name")
    val name: String
) {
    override fun toString(): String {
        return "$id: $name"
    }
}
