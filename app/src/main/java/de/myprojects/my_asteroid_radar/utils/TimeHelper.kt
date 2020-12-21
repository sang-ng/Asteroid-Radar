package de.myprojects.my_asteroid_radar.utils

import android.util.Log
import de.myprojects.my_asteroid_radar.utils.TimeHelper.dateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)

    fun getDateToday(): String {


        Log.i("TEST", dateFormat.format(calendar.time))

        return dateFormat.format(calendar.time)
    }

    fun getNextSevenDays(): String {

        calendar.add(Calendar.DATE, 7)
        Log.i("TEST", dateFormat.format(calendar.time))


        return dateFormat.format(calendar.time)
    }
}