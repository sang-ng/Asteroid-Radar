package de.myprojects.my_asteroid_radar.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)

    fun getDateToday(): String {
        return dateFormat.format(calendar.time)
    }

    fun getNextSevenDays(): String {
        calendar.add(Calendar.DATE, 7)

        return dateFormat.format(calendar.time)
    }
}