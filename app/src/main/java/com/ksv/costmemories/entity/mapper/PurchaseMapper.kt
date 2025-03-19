package com.ksv.costmemories.entity.mapper

import com.ksv.costmemories.entity.PurchaseCsv
import com.ksv.costmemories.entity.PurchaseTuple
import com.ksv.costmemories.util.DateUtils

fun PurchaseTuple.toCsv() = PurchaseCsv(
    id = id,
    date = DateUtils.millsToLongDateFormat(milliseconds),
    product = product,
    title = title,
    shop = shop,
    cost = cost,
    comment = comment
)

fun PurchaseCsv.toTuple() = PurchaseTuple(
    id = id,
    milliseconds = DateUtils.longDateFormatToMills(date),
    product = product,
    title = title,
    shop = shop,
    cost = cost,
    comment = comment
)