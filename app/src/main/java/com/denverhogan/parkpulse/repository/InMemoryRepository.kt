package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.network.GetAllParksResult
import com.denverhogan.parkpulse.network.GetAllRidesResult
import com.denverhogan.parkpulse.network.QueueTimesApi
import javax.inject.Inject

class InMemoryRepository @Inject constructor(private val api: QueueTimesApi) :
    Repository {
    override suspend fun getAllParks(): GetAllParksResult {
        try {
            val response = api.getAllParks()
            val parentCompanies = response.body()!!.parentCompanies
            val parks = parentCompanies[0].parks
            if (response.isSuccessful && response.body() != null) {
                return GetAllParksResult.Success(parks)
            }

            throw Exception("Unsuccessful response")
        } catch (t: Throwable) {
            return GetAllParksResult.Error(t.message.toString())
        }
    }

    override suspend fun getAllRides(id: Int): GetAllRidesResult {
        try {
            val response = api.getAllRides(id)
            val lands = response.body()!!.lands
            val coasters = lands[0].rides
            val family = lands[1].rides
            val kids = lands[2].rides
            val other = lands[3].rides
            val thrill = lands[4].rides
            if (response.isSuccessful && response.body() != null) {
                return GetAllRidesResult.Success(rides = coasters + thrill + family)
            }

            throw Exception("Unsuccessful response")
        } catch (t: Throwable) {
            return GetAllRidesResult.Error(t.message.toString())
        }
    }
}
