package com.denverhogan.parkpulse.network

import com.denverhogan.parkpulse.model.Ride

sealed interface GetAllRidesResult {
    data class Success(val rides: List<Ride>) : GetAllRidesResult
    data class Error(val message: String) : GetAllRidesResult
}
