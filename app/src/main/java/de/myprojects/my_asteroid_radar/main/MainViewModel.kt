package de.myprojects.my_asteroid_radar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.myprojects.my_asteroid_radar.database.getDatabase
import de.myprojects.my_asteroid_radar.domain.Asteroid
import de.myprojects.my_asteroid_radar.domain.PictureOfDay
import de.myprojects.my_asteroid_radar.network.NasaApi
import de.myprojects.my_asteroid_radar.utils.Constants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)

    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    private val _imageOfDay = MutableLiveData<PictureOfDay>()

    init {

        getAsteroids()
        getImageOfDay()

        getNextSevenDays()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                val response = NasaApi.retrofitService.getAsteroids(
                    getDateToday(),
                    getNextSevenDays(),
                    "DEMO_KEY"
                )

            } catch (e: Exception) {
                Log.i("TEST", e.toString())
            }
        }
    }

    private fun getDateToday(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        return dateFormat.format(currentTime)
    }

    private fun getNextSevenDays(): String {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time

        calendar.add(Calendar.DATE, 7)

        return dateFormat.format(calendar.time)
    }

    private fun getImageOfDay() {
        viewModelScope.launch {
            try {
                val response = NasaApi.retrofitService.getImageOfDay("DEMO_KEY")
                _imageOfDay.value = response

            } catch (e: Exception) {
                Log.i("TEST", e.toString())
            }
        }
    }
}