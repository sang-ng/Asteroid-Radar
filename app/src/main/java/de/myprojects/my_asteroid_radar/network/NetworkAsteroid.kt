package de.myprojects.my_asteroid_radar.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.myprojects.my_asteroid_radar.database.DatabaseAsteroid
import de.myprojects.my_asteroid_radar.domain.Asteroid
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)

@Parcelize
data class NetworkAsteroid(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val codename: String,
    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @Json(name = "relative_velocity")
    val relativeVelocity: Double,
    @Json(name = "miss_distance")
    val distanceFromEarth: Double,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean
) : Parcelable


fun NetworkAsteroidContainer.asDatabaseModel(): List<DatabaseAsteroid> {
    return asteroids.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}