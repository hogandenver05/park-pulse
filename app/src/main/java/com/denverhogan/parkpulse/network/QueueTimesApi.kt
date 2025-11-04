package com.denverhogan.parkpulse.network

import com.denverhogan.parkpulse.model.ParksResponse
import com.denverhogan.parkpulse.model.LandsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QueueTimesApi {
    @GET("parks/")
    suspend fun getAllParks(): Response<ParksResponse>

    @GET("parks/{id}/queue_times.json")
    suspend fun getAllRides(@Path("id") id: Int): Response<LandsResponse>
}
