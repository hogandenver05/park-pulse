package com.denverhogan.themeparks.network

import com.denverhogan.themeparks.model.Ride

sealed interface GetAllRidesResult {
    data class Success(val rides: List<Ride>) : GetAllRidesResult
    data class Error(val message: String) : GetAllRidesResult
}