package de.myprojects.my_asteroid_radar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import de.myprojects.my_asteroid_radar.utils.Constants
import de.myprojects.my_asteroid_radar.domain.PictureOfDay
import de.myprojects.my_asteroid_radar.network.NasaApi
import de.myprojects.my_asteroid_radar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

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