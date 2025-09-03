package com.denverhogan.themeparks.repository

import com.denverhogan.themeparks.model.GetAllDestinationsResult

interface DestinationsListRepository {
    suspend fun getAllDestinations(): GetAllDestinationsResult
}