package de.myprojects.my_asteroid_radar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.myprojects.my_asteroid_radar.database.getDatabase
import de.myprojects.my_asteroid_radar.domain.PictureOfDay
import de.myprojects.my_asteroid_radar.network.NasaApi
import de.myprojects.my_asteroid_radar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    val asteroids = repository.asteroids

    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    private val _imageOfDay = MutableLiveData<PictureOfDay>()

    init {
        getAsteroids()
        getImageOfDay()

//        getNextSevenDays()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
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