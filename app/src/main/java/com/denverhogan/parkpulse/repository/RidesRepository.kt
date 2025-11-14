package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.model.Ride
import com.denverhogan.parkpulse.network.QueueTimesApi
import javax.inject.Inject

class RidesRepository @Inject constructor(private val api: QueueTimesApi) {
    suspend fun getRides(parkId: Int): List<Ride> {
        val response = api.getAllRides(parkId)
        if (response.isSuccessful) {
            return response.body()?.lands?.flatMap { it.rides } ?: emptyList()
        }
        return emptyList()
    }
}
