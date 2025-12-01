package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.model.Land
import com.denverhogan.parkpulse.model.LandsResponse
import com.denverhogan.parkpulse.model.Ride
import com.denverhogan.parkpulse.network.QueueTimesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class RidesRepositoryTest {

    private lateinit var api: QueueTimesApi
    private lateinit var repository: RidesRepository

    @Before
    fun setup() {
        api = mock()
        repository = RidesRepository(api)
    }

    @Test
    fun `getRides returns rides from successful response`() = runTest {
        val rides = listOf(
            createRide(1, "Ride A", isOpen = true, waitTime = 30),
            createRide(2, "Ride B", isOpen = false, waitTime = 0)
        )
        val land = Land(1, "Land A", rides)
        val landsResponse = LandsResponse(lands = listOf(land), rides = emptyList())
        val response = Response.success(landsResponse)
        whenever(api.getAllRides(1)).thenReturn(response)

        val result = repository.getRides(1)

        assertEquals(2, result.size)
        assertEquals("Ride A", result[0].name)
        assertEquals("Ride B", result[1].name)
    }

    @Test
    fun `getRides returns empty list when response is not successful`() = runTest {
        val response = Response.error<LandsResponse>(500, mock())
        whenever(api.getAllRides(1)).thenReturn(response)

        val result = repository.getRides(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getRides returns empty list when response body is null`() = runTest {
        val response = Response.success<LandsResponse>(null)
        whenever(api.getAllRides(1)).thenReturn(response)

        val result = repository.getRides(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getRides handles multiple lands`() = runTest {
        val rides1 = listOf(createRide(1, "Ride A"))
        val rides2 = listOf(createRide(2, "Ride B"))
        val lands = listOf(
            Land(1, "Land A", rides1),
            Land(2, "Land B", rides2)
        )
        val landsResponse = LandsResponse(lands = lands, rides = emptyList())
        val response = Response.success(landsResponse)
        whenever(api.getAllRides(1)).thenReturn(response)

        val result = repository.getRides(1)

        assertEquals(2, result.size)
    }

    @Test
    fun `getRides handles empty lands list`() = runTest {
        val landsResponse = LandsResponse(lands = emptyList(), rides = emptyList())
        val response = Response.success(landsResponse)
        whenever(api.getAllRides(1)).thenReturn(response)

        val result = repository.getRides(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getRides uses correct parkId`() = runTest {
        val rides = listOf(createRide(1, "Ride A"))
        val land = Land(1, "Land A", rides)
        val landsResponse = LandsResponse(lands = listOf(land), rides = emptyList())
        val response = Response.success(landsResponse)
        whenever(api.getAllRides(5)).thenReturn(response)

        val result = repository.getRides(5)

        assertEquals(1, result.size)
    }

    private fun createRide(
        id: Int,
        name: String,
        isOpen: Boolean = true,
        waitTime: Int = 0,
        lastUpdated: String = "2024-01-01T00:00:00Z"
    ): Ride {
        return Ride(
            id = id,
            name = name,
            isOpen = isOpen,
            waitTime = waitTime,
            lastUpdated = lastUpdated
        )
    }
}

