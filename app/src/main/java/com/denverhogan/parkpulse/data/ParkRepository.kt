package com.denverhogan.parkpulse.data

import android.location.Location
import com.denverhogan.parkpulse.data.local.FavoriteParkDao
import com.denverhogan.parkpulse.location.LocationProvider
import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.network.QueueTimesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParkRepository @Inject constructor(
    private val queueTimesApi: QueueTimesApi,
    private val favoriteParkDao: FavoriteParkDao,
    private val locationProvider: LocationProvider
) {

    suspend fun getParks(): List<Park> {
        val favoriteParkIds = favoriteParkDao.getFavoriteParkIds().first()
        val location = locationProvider.lastLocation()?.await()
        val parks = queueTimesApi.getAllParks().body()?.flatMap { it.parks } ?: emptyList()

        return parks.map {
            val distance = if (location != null) {
                val parkLocation = Location("").apply {
                    latitude = it.latitude.toDouble()
                    longitude = it.longitude.toDouble()
                }
                // Convert to Double after calculation
                location.distanceTo(parkLocation).toDouble() / 1000
            } else {
                null
            }
            it.copy(
                isFavorite = favoriteParkIds.contains(it.id),
                distance = distance
            )

        }
    }

    fun getFavoriteParkIds(): Flow<List<Int>> {
        return favoriteParkDao.getFavoriteParkIds()
    }

    suspend fun addFavorite(parkId: Int) {
        favoriteParkDao.addFavorite(com.denverhogan.parkpulse.data.local.FavoritePark(parkId))
    }

    suspend fun removeFavorite(parkId: Int) {
        favoriteParkDao.removeFavorite(parkId)
    }
}
