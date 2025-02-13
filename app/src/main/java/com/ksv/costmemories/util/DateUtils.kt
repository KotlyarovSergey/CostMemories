package com.ksv.costmemories.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

object DateUtils {
    fun dateFromMillsToString(mills: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = mills
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formattedText = LocalDate.of(year, month, day).format(formatter)
        return formattedText
    }
}