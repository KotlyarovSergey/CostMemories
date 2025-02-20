package com.ksv.costmemories.ui.database.entity

data class DbItem(
    val id: Long,
    val text: String,
    val counter: Long,
    val type: DbItemType
)
