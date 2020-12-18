package de.myprojects.my_asteroid_radar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import de.myprojects.my_asteroid_radar.database.AsteroidRoom
import de.myprojects.my_asteroid_radar.database.asDomainModel
import de.myprojects.my_asteroid_radar.domain.Asteroid
import de.myprojects.my_asteroid_radar.network.NasaApi
import de.myprojects.my_asteroid_radar.network.parseAsteroidsJsonResult
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
                NasaApi.retrofitService.getAsteroids("2020-12-19", "2020-12-25", "DEMO_KEY")

            val jsonResult = parseAsteroidsJsonResult(JSONObject(asteroids))

            database.asteroidDao.insertAll(jsonResult)
        }
    }

    suspend fun deleteAll(){
        withContext(Dispatchers.IO){
            database.asteroidDao.deleteAll()
        }
    }
}