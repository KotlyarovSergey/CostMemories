package com.ksv.costmemories.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "titles",
    indices = [Index("title")]
)
data class Title(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String
)
