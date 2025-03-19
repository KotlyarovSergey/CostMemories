package com.ksv.costmemories.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    fun longDateFormatToMills(date: String): Long{
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return try {
            val dateParsed = sdf.parse(date)
            val mills = dateParsed?.time ?: 0
            mills
        } catch (exception: Exception){
            0
        }
    }

    fun shortDateFormatToMills(date: String): Long{
        val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        return try {
            val dateParsed = sdf.parse(date)
            val mills = dateParsed?.time ?: 0
            mills
        } catch (exception: Exception){
            0
        }
    }

    fun millsToShortDateFormat(mills: Long): String{
        val sdf = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val date = Date.from(Instant.ofEpochMilli(mills))
        val dateText = sdf.format(date)
        return dateText
    }

    fun millsToLongDateFormat(mills: Long): String{
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = Date.from(Instant.ofEpochMilli(mills))
        val dateText = sdf.format(date)
        return dateText
    }
}