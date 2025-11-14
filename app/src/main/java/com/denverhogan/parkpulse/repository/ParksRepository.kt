package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.network.QueueTimesApi
import javax.inject.Inject

class ParksRepository @Inject constructor(private val api: QueueTimesApi) {
    suspend fun getParks(): List<Park> {
        val response = api.getAllParks()
        if (response.isSuccessful) {
            return response.body()?.flatMap { it.parks } ?: emptyList()
        }
        return emptyList()
    }
}