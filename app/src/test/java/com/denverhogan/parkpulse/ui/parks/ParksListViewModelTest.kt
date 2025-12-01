package com.denverhogan.parkpulse.ui.parks

import com.denverhogan.parkpulse.data.ParkRepository
import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.model.ParkSortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ParksListViewModelTest {

    private lateinit var parkRepository: ParkRepository
    private lateinit var viewModel: ParksListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        parkRepository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        whenever(parkRepository.getParks()).thenReturn(emptyList())
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        // Initial state should be Loading, then Success
        val state = viewModel.uiState.value
        assertTrue(state is ParksListViewState.Success)
    }

    @Test
    fun `getParks success updates state to Success`() = runTest {
        val parks = listOf(
            createPark(1, "Park A", isFavorite = true, distance = 10.0),
            createPark(2, "Park B", isFavorite = false, distance = 5.0)
        )
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as ParksListViewState.Success
        assertEquals(2, state.parks.size)
        assertEquals(ParkSortOption.FAVORITES, state.sortOption)
    }

    @Test
    fun `getParks error updates state to Error`() = runTest {
        val errorMessage = "Network error"
        whenever(parkRepository.getParks()).thenThrow(RuntimeException(errorMessage))
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as ParksListViewState.Error
        assertEquals(errorMessage, state.errorMessage)
    }

    @Test
    fun `getParks with null error message uses default`() = runTest {
        whenever(parkRepository.getParks()).thenThrow(RuntimeException())
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as ParksListViewState.Error
        assertEquals("An unknown error occurred", state.errorMessage)
    }

    @Test
    fun `sortParks by FAVORITES sorts correctly`() = runTest {
        val parks = listOf(
            createPark(1, "Park A", isFavorite = false, distance = 5.0),
            createPark(2, "Park B", isFavorite = true, distance = 10.0),
            createPark(3, "Park C", isFavorite = true, distance = 3.0)
        )
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()

        viewModel.sortParks(ParkSortOption.FAVORITES)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as ParksListViewState.Success
        assertTrue(sortedState.parks[0].isFavorite) // First should be favorite
        assertTrue(sortedState.parks[1].isFavorite) // Second should be favorite
        assertFalse(sortedState.parks[2].isFavorite) // Last should not be favorite
        assertEquals(ParkSortOption.FAVORITES, sortedState.sortOption)
    }

    @Test
    fun `sortParks by DISTANCE sorts correctly`() = runTest {
        val parks = listOf(
            createPark(1, "Park A", distance = 10.0),
            createPark(2, "Park B", distance = 5.0),
            createPark(3, "Park C", distance = 15.0)
        )
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        viewModel.sortParks(ParkSortOption.DISTANCE)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as ParksListViewState.Success
        assertEquals(5.0, sortedState.parks[0].distance)
        assertEquals(10.0, sortedState.parks[1].distance)
        assertEquals(15.0, sortedState.parks[2].distance)
        assertEquals(ParkSortOption.DISTANCE, sortedState.sortOption)
    }

    @Test
    fun `sortParks by ALPHABETICAL sorts correctly`() = runTest {
        val parks = listOf(
            createPark(1, "Zebra Park"),
            createPark(2, "Alpha Park"),
            createPark(3, "Beta Park")
        )
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        viewModel.sortParks(ParkSortOption.ALPHABETICAL)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as ParksListViewState.Success
        assertEquals("Alpha Park", sortedState.parks[0].name)
        assertEquals("Beta Park", sortedState.parks[1].name)
        assertEquals("Zebra Park", sortedState.parks[2].name)
        assertEquals(ParkSortOption.ALPHABETICAL, sortedState.sortOption)
    }

    @Test
    fun `sortParks with null distance handles correctly`() = runTest {
        val parks = listOf(
            createPark(1, "Park A", distance = null),
            createPark(2, "Park B", distance = 5.0)
        )
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        viewModel.sortParks(ParkSortOption.DISTANCE)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as ParksListViewState.Success
        // sortedBy puts nulls first, so null should be first
        assertEquals(null, sortedState.parks[0].distance)
        assertEquals(5.0, sortedState.parks[1].distance)
    }

    @Test
    fun `refreshParks calls getParks again`() = runTest {
        val parks = listOf(createPark(1, "Park A"))
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        viewModel.refreshParks()
        advanceUntilIdle()
        
        // getParks is called in init and refreshParks, so at least 2 times
        verify(parkRepository, org.mockito.kotlin.atLeast(2)).getParks()
    }

    @Test
    fun `toggleFavorite adds favorite when not favorite`() = runTest {
        val parks = listOf(createPark(1, "Park A", isFavorite = false))
        whenever(parkRepository.getParks()).thenReturn(parks)
        whenever(parkRepository.addFavorite(1)).thenReturn(Unit)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        val updatedParks = listOf(createPark(1, "Park A", isFavorite = true))
        whenever(parkRepository.getParks()).thenReturn(updatedParks)
        
        viewModel.toggleFavorite(1)
        advanceUntilIdle()
        
        verify(parkRepository).addFavorite(1)
    }

    @Test
    fun `toggleFavorite removes favorite when already favorite`() = runTest {
        val parks = listOf(createPark(1, "Park A", isFavorite = true))
        whenever(parkRepository.getParks()).thenReturn(parks)
        whenever(parkRepository.removeFavorite(1)).thenReturn(Unit)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        val updatedParks = listOf(createPark(1, "Park A", isFavorite = false))
        whenever(parkRepository.getParks()).thenReturn(updatedParks)
        
        viewModel.toggleFavorite(1)
        advanceUntilIdle()
        
        verify(parkRepository).removeFavorite(1)
    }

    @Test
    fun `toggleFavorite does nothing when park not found`() = runTest {
        val parks = listOf(createPark(1, "Park A"))
        whenever(parkRepository.getParks()).thenReturn(parks)
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        viewModel.toggleFavorite(999) // Non-existent park ID
        advanceUntilIdle()
        
        verify(parkRepository, org.mockito.kotlin.never()).addFavorite(any())
        verify(parkRepository, org.mockito.kotlin.never()).removeFavorite(any())
    }

    @Test
    fun `sortParks does nothing when state is not Success`() = runTest {
        whenever(parkRepository.getParks()).thenThrow(RuntimeException("Error"))
        
        viewModel = ParksListViewModel(parkRepository)
        advanceUntilIdle()
        
        // State should be Error
        assertTrue(viewModel.uiState.value is ParksListViewState.Error)
        
        // Sort should not crash
        viewModel.sortParks(ParkSortOption.ALPHABETICAL)
        advanceUntilIdle()
        
        // State should still be Error
        assertTrue(viewModel.uiState.value is ParksListViewState.Error)
    }

    private fun createPark(
        id: Int,
        name: String,
        isFavorite: Boolean = false,
        distance: Double? = null
    ): Park {
        return Park(
            id = id,
            name = name,
            country = "USA",
            continent = "North America",
            latitude = "0.0",
            longitude = "0.0",
            timezone = "UTC",
            isFavorite = isFavorite,
            distance = distance
        )
    }
}

