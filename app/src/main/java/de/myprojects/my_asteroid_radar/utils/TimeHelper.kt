package de.myprojects.my_asteroid_radar.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {
    fun getDateToday(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        return dateFormat.format(currentTime)
    }

    fun getNextSevenDays(): String {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time

        calendar.add(Calendar.DATE, 7)

        return dateFormat.format(calendar.time)
    }
}