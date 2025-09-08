package com.denverhogan.themeparks.repository

import com.denverhogan.themeparks.network.GetAllRidesResult

interface RidesListRepository {
    suspend fun getAllRides(): GetAllRidesResult
}