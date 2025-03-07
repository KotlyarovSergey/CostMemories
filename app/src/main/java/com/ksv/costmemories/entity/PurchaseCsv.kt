package com.ksv.costmemories.entity

import com.opencsv.bean.CsvBindByName

data class PurchaseCsv(
    @CsvBindByName
    val id: Long,
    @CsvBindByName
    val date: String,
    @CsvBindByName
    val product: String,
    @CsvBindByName
    val title: String,
    @CsvBindByName
    val shop: String,
    @CsvBindByName
    val cost: Int,
    @CsvBindByName
    val comment: String = "",
){
    constructor() : this(0, "", "", "", "", 0, "")
}