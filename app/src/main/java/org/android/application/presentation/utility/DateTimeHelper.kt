package org.android.application.presentation.utility

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class DateTimeHelper {

    companion object {

        const val YYYYMMDD = "yyyy-MM-dd"
        const val DDMMYYYY = "dd-MM-yyyy"
        const val DDMMMYYYY = "dd MMM yyyy"
        const val HHMMA = "hh:mm a"
        const val DDMMMYYYYHHMMA = "MMMMM dd, hh:mm a"
        const val MMMMMDD = "MMMMM dd"


        /**
         * For Datetime convertor
         *
         * @param time in milles
         */
        fun String.getFormattedDatetime(inputFormat: String, outputFormat: String): String {
            var outputString = this
            try {
                val inputFormatter = DateTimeFormat.forPattern(inputFormat)
                val inputDatetime = inputFormatter.parseDateTime(this)
                val outputFormatter: DateTimeFormatter = DateTimeFormat.forPattern(outputFormat)
                outputString = outputFormatter.print(inputDatetime)
            } catch (e: Exception) {
                e.printStackTrace()
                outputString = this
            }
            return outputString
        }

        fun String.getFormattedDatetime(outputFormat: String): String {
            var longtime: Long = 0L
            if (this.isNotEmpty()) {
                this.toLong().apply {
                    longtime = this
                }
            }
            if (longtime < 1000000000000L) {
                longtime *= 1000
            }
            val dateTime = DateTime(longtime)
            return dateTime.toString(outputFormat)
        }
    }
}