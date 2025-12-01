package com.denverhogan.parkpulse.repository

import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.model.ParentCompany
import com.denverhogan.parkpulse.network.QueueTimesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class ParksRepositoryTest {

    private lateinit var api: QueueTimesApi
    private lateinit var repository: ParksRepository

    @Before
    fun setup() {
        api = mock()
        repository = ParksRepository(api)
    }

    @Test
    fun `getParks returns parks from successful response`() = runTest {
        val parks = listOf(
            createPark(1, "Park A"),
            createPark(2, "Park B")
        )
        val parentCompany = ParentCompany(1, "Company", parks)
        val response = Response.success(listOf(parentCompany))
        whenever(api.getAllParks()).thenReturn(response)

        val result = repository.getParks()

        assertEquals(2, result.size)
        assertEquals("Park A", result[0].name)
        assertEquals("Park B", result[1].name)
    }

    @Test
    fun `getParks returns empty list when response is not successful`() = runTest {
        val response = Response.error<List<ParentCompany>>(500, mock())
        whenever(api.getAllParks()).thenReturn(response)

        val result = repository.getParks()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getParks returns empty list when response body is null`() = runTest {
        val response = Response.success<List<ParentCompany>>(null)
        whenever(api.getAllParks()).thenReturn(response)

        val result = repository.getParks()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getParks handles multiple parent companies`() = runTest {
        val parks1 = listOf(createPark(1, "Park A"))
        val parks2 = listOf(createPark(2, "Park B"))
        val parentCompanies = listOf(
            ParentCompany(1, "Company 1", parks1),
            ParentCompany(2, "Company 2", parks2)
        )
        val response = Response.success(parentCompanies)
        whenever(api.getAllParks()).thenReturn(response)

        val result = repository.getParks()

        assertEquals(2, result.size)
    }

    @Test
    fun `getParks handles empty parent companies list`() = runTest {
        val response = Response.success<List<ParentCompany>>(emptyList())
        whenever(api.getAllParks()).thenReturn(response)

        val result = repository.getParks()

        assertTrue(result.isEmpty())
    }

    private fun createPark(id: Int, name: String): Park {
        return Park(
            id = id,
            name = name,
            country = "USA",
            continent = "North America",
            latitude = "0.0",
            longitude = "0.0",
            timezone = "UTC"
        )
    }
}

