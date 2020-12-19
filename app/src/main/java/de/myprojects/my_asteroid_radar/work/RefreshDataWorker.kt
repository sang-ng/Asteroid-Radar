package de.myprojects.my_asteroid_radar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import de.myprojects.my_asteroid_radar.database.getDatabase
import de.myprojects.my_asteroid_radar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}