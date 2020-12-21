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
import de.myprojects.my_asteroid_radar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    enum class Connection { SUCCESS, ERROR }

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    val asteroids = repository.asteroids

    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    val navigateToDetail: LiveData<Asteroid>
        get() = _navigateToDetail

    val connectionError: LiveData<Connection>
        get() = _connectionError

    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    private val _navigateToDetail = MutableLiveData<Asteroid>()
    private val _connectionError = MutableLiveData<Connection>()

    init {
        getAsteroids()
        getImageOfDay()
    }

    private fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids()

                _connectionError.value = Connection.SUCCESS
            } catch (e: Exception) {
                Log.i("TEST", e.toString())
                _connectionError.value = Connection.ERROR
            }
            Log.i("TEST", "vm" + _connectionError.value.toString())
        }
    }

    private fun getImageOfDay() {
        viewModelScope.launch {
            try {
                val response = NasaApi.retrofitService.getImageOfDay("DEMO_KEY")
                _imageOfDay.value = response

            } catch (e: Exception) {
                Log.i("TEST", "getImageOfDay: $e")
            }
        }
    }

    fun onListItemClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onDetailNavigated() {
        _navigateToDetail.value = null
    }
}