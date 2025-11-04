package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.network.GetAllParksResult
import com.denverhogan.parkpulse.network.GetAllRidesResult

interface Repository {
    suspend fun getAllParks(): GetAllParksResult
    suspend fun getAllRides(id: Int): GetAllRidesResult
}
