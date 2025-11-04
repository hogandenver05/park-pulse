package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.network.GetAllRidesResult

interface RidesListRepository {
    suspend fun getAllRides(): GetAllRidesResult
}