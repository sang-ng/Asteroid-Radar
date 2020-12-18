package de.myprojects.my_asteroid_radar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidRoom : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidRoom

fun getDatabase(context: Context): AsteroidRoom {
    synchronized(AsteroidRoom::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidRoom::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}