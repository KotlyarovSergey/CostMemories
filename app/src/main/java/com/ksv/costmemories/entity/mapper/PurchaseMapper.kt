package com.ksv.costmemories.entity.mapper

import com.ksv.costmemories.entity.PurchaseCsv
import com.ksv.costmemories.entity.PurchaseTuple

fun PurchaseTuple.toCsv() = PurchaseCsv(
    id = id,
    date = date,
    product = product,
    title = title,
    shop = shop,
    cost = cost,
    comment = comment
)

fun PurchaseCsv.toTuple() = PurchaseTuple(
    id = id,
    date = date,
    product = product,
    title = title,
    shop = shop,
    cost = cost,
    comment = comment
)