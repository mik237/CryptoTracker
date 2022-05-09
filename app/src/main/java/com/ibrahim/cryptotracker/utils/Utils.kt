package com.ibrahim.cryptotracker.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun formatDateTime(timeStamp: String): String{
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
        val outputPattern = "dd MMM yyyy, hh:mm a"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("GMT")
        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        var formattedDateTime = ""

        try {
            val date = inputFormat.parse(timeStamp)
            if (date != null) {
                formattedDateTime = outputFormat.format(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formattedDateTime
    }

    fun currencySymbol(code: String): String{
        val currency = Currency.getInstance(code)
        return currency.symbol
    }
}