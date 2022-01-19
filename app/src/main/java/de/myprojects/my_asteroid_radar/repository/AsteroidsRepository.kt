package de.myprojects.my_asteroid_radar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import de.myprojects.my_asteroid_radar.database.AsteroidRoom
import de.myprojects.my_asteroid_radar.database.asDomainModel
import de.myprojects.my_asteroid_radar.domain.Asteroid
import de.myprojects.my_asteroid_radar.network.NasaApi
import de.myprojects.my_asteroid_radar.network.parseAsteroidsJsonResult
import de.myprojects.my_asteroid_radar.utils.Constants
import de.myprojects.my_asteroid_radar.utils.TimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidRoom) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids =
                NasaApi.retrofitService.getAsteroids(
                    TimeHelper.getDateToday(),
                    TimeHelper.getNextSevenDays(),
                    Constants.API_KEY
                )

            val jsonResult = parseAsteroidsJsonResult(JSONObject(asteroids))

            database.asteroidDao.insertAll(jsonResult)
        }
    }
}