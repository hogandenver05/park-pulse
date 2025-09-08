package com.denverhogan.themeparks.network

import com.denverhogan.themeparks.model.LandsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ThemeParksApi {
    @GET("60/queue_times.json")
    suspend fun getAllRides(): Response<LandsResponse>
}