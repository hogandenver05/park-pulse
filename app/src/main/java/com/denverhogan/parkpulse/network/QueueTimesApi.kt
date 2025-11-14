package com.denverhogan.parkpulse.network

import com.denverhogan.parkpulse.model.LandsResponse
import com.denverhogan.parkpulse.model.ParentCompany
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QueueTimesApi {
    @GET("parks.json")
    suspend fun getAllParks(): Response<List<ParentCompany>>

    @GET("parks/{id}/queue_times.json")
    suspend fun getAllRides(@Path("id") id: Int): Response<LandsResponse>
}
