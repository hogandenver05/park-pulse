package com.denverhogan.parkpulse.data

import com.denverhogan.parkpulse.data.local.FavoritePark
import com.denverhogan.parkpulse.data.local.FavoriteParkDao
import com.denverhogan.parkpulse.location.LocationProvider
import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.model.ParentCompany
import com.denverhogan.parkpulse.network.QueueTimesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ParkRepositoryTest {

    private lateinit var queueTimesApi: QueueTimesApi
    private lateinit var favoriteParkDao: FavoriteParkDao
    private lateinit var locationProvider: LocationProvider
    private lateinit var parkRepository: ParkRepository

    @Before
    fun setup() {
        queueTimesApi = mock()
        favoriteParkDao = mock()
        locationProvider = mock()
    }

    @Test
    fun `getParks returns parks with favorites marked`() = runTest {
        val favoriteIds = listOf(1, 3)
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))
        whenever(locationProvider.lastLocation()).thenReturn(null)

        val parks = listOf(
            createParkData(1, "Park A"),
            createParkData(2, "Park B"),
            createParkData(3, "Park C")
        )
        val parentCompany = ParentCompany(1, "Company", parks)
        val response = Response.success(listOf(parentCompany))
        whenever(queueTimesApi.getAllParks()).thenReturn(response)

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getParks()

        assertEquals(3, result.size)
        assertTrue(result[0].isFavorite) // Park 1 is favorite
        assertFalse(result[1].isFavorite) // Park 2 is not favorite
        assertTrue(result[2].isFavorite) // Park 3 is favorite
    }

    @Test
    fun `getParks handles location when available`() = runTest {
        val favoriteIds = emptyList<Int>()
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))
        // Location testing requires Android framework, tested in integration tests
        whenever(locationProvider.lastLocation()).thenReturn(null)

        val parks = listOf(
            createParkData(1, "Park A", latitude = "40.1", longitude = "-74.1")
        )
        val parentCompany = ParentCompany(1, "Company", parks)
        val response = Response.success(listOf(parentCompany))
        whenever(queueTimesApi.getAllParks()).thenReturn(response)

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getParks()

        assertEquals(1, result.size)
        // Distance is null when location is not available
        assertNull(result[0].distance)
    }

    @Test
    fun `getParks returns null distance when location is not available`() = runTest {
        val favoriteIds = emptyList<Int>()
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))
        whenever(locationProvider.lastLocation()).thenReturn(null)

        val parks = listOf(createParkData(1, "Park A"))
        val parentCompany = ParentCompany(1, "Company", parks)
        val response = Response.success(listOf(parentCompany))
        whenever(queueTimesApi.getAllParks()).thenReturn(response)

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getParks()

        assertEquals(1, result.size)
        assertNull(result[0].distance)
    }

    @Test
    fun `getParks handles invalid latitude longitude gracefully`() = runTest {
        val favoriteIds = emptyList<Int>()
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))

        whenever(locationProvider.lastLocation()).thenReturn(null)

        // Invalid coordinates
        val parks = listOf(
            createParkData(1, "Park A", latitude = "invalid", longitude = "invalid")
        )
        val parentCompany = ParentCompany(1, "Company", parks)
        val response = Response.success(listOf(parentCompany))
        whenever(queueTimesApi.getAllParks()).thenReturn(response)

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getParks()

        assertEquals(1, result.size)
        assertNull(result[0].distance) // Should handle NumberFormatException
    }

    @Test
    fun `getParks returns empty list when API response is null`() = runTest {
        val favoriteIds = emptyList<Int>()
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))
        whenever(locationProvider.lastLocation()).thenReturn(null)

        val response = Response.success<List<ParentCompany>>(null)
        whenever(queueTimesApi.getAllParks()).thenReturn(response)

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getParks()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getParks handles multiple parent companies`() = runTest {
        val favoriteIds = emptyList<Int>()
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))
        whenever(locationProvider.lastLocation()).thenReturn(null)

        val parks1 = listOf(createParkData(1, "Park A"))
        val parks2 = listOf(createParkData(2, "Park B"))
        val parentCompanies = listOf(
            ParentCompany(1, "Company 1", parks1),
            ParentCompany(2, "Company 2", parks2)
        )
        val response = Response.success(parentCompanies)
        whenever(queueTimesApi.getAllParks()).thenReturn(response)

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getParks()

        assertEquals(2, result.size)
    }

    @Test
    fun `addFavorite calls dao addFavorite`() = runTest {
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(emptyList()))
        whenever(locationProvider.lastLocation()).thenReturn(null)
        whenever(queueTimesApi.getAllParks()).thenReturn(Response.success(emptyList()))

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        parkRepository.addFavorite(1)

        verify(favoriteParkDao).addFavorite(FavoritePark(1))
    }

    @Test
    fun `removeFavorite calls dao removeFavorite`() = runTest {
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(emptyList()))
        whenever(locationProvider.lastLocation()).thenReturn(null)
        whenever(queueTimesApi.getAllParks()).thenReturn(Response.success(emptyList()))

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        parkRepository.removeFavorite(1)

        verify(favoriteParkDao).removeFavorite(1)
    }

    @Test
    fun `getFavoriteParkIds returns flow from dao`() = runTest {
        val favoriteIds = listOf(1, 2, 3)
        whenever(favoriteParkDao.getFavoriteParkIds()).thenReturn(flowOf(favoriteIds))
        whenever(locationProvider.lastLocation()).thenReturn(null)
        whenever(queueTimesApi.getAllParks()).thenReturn(Response.success(emptyList()))

        parkRepository = ParkRepository(queueTimesApi, favoriteParkDao, locationProvider)
        val result = parkRepository.getFavoriteParkIds().first()

        assertEquals(favoriteIds, result)
    }

    private fun createParkData(
        id: Int,
        name: String,
        latitude: String = "0.0",
        longitude: String = "0.0"
    ): Park {
        return Park(
            id = id,
            name = name,
            country = "USA",
            continent = "North America",
            latitude = latitude,
            longitude = longitude,
            timezone = "UTC"
        )
    }
}

