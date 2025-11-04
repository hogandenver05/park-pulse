package com.denverhogan.parkpulse.network

import com.denverhogan.parkpulse.model.LandsResponse
import retrofit2.Response
import retrofit2.http.GET

interface QueueTimesApi {
    @GET("60/queue_times.json")
    suspend fun getAllRides(): Response<LandsResponse>
}