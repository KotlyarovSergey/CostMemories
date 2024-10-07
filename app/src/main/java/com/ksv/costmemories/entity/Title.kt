package com.ksv.costmemories.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "titles",
    indices = [Index(value = ["text"], unique = true)])
data class Title(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String
)
