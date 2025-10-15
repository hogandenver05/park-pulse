package com.denverhogan.themeparks.repository

import com.denverhogan.themeparks.network.GetAllRidesResult
import com.denverhogan.themeparks.network.ThemeParksApi
import javax.inject.Inject

class InMemoryRidesListRepository @Inject constructor(private val api: ThemeParksApi) : RidesListRepository {
    override suspend fun getAllRides(): GetAllRidesResult {
        try {
            val response = api.getAllRides()
            val lands = response.body()!!.lands
            val coasters = lands[0].rides
            val family = lands[1].rides
            val kids = lands[2].rides
            val other = lands[3].rides
            val thrill = lands[4].rides
            return if (response.isSuccessful && response.body() != null) {
                GetAllRidesResult.Success(rides = coasters + thrill + family)
            } else {
                GetAllRidesResult.Error(message = "Something went wrong")
            }
        } catch (t: Throwable) {
            return GetAllRidesResult.Error(t.message.toString())
        }
    }
}