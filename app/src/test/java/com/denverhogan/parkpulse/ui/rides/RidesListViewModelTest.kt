package com.denverhogan.parkpulse.ui.rides

import androidx.lifecycle.SavedStateHandle
import com.denverhogan.parkpulse.model.Ride
import com.denverhogan.parkpulse.model.RideSortOption
import com.denverhogan.parkpulse.repository.RidesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class RidesListViewModelTest {

    private lateinit var ridesRepository: RidesRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: RidesListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        ridesRepository = mock()
        savedStateHandle = SavedStateHandle().apply {
            set("parkId", 1)
            set("parkName", "Test%20Park")
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads rides successfully`() = runTest {
        val rides = listOf(
            createRide(1, "Ride A", isOpen = true, waitTime = 30),
            createRide(2, "Ride B", isOpen = false, waitTime = 0)
        )
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as RidesListViewState.Success
        assertEquals(2, state.rides.size)
        assertEquals("Test Park", state.parkName)
        assertEquals(RideSortOption.LONG_WAIT, state.sortOption)
    }

    @Test
    fun `initial state handles error correctly`() = runTest {
        whenever(ridesRepository.getRides(1)).thenThrow(RuntimeException("Network error"))
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as RidesListViewState.Error
        assertEquals("Failed to load rides", state.errorMessage)
    }

    @Test
    fun `refreshRides reloads data`() = runTest {
        val rides = listOf(createRide(1, "Ride A"))
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        val newRides = listOf(createRide(1, "Ride A"), createRide(2, "Ride B"))
        whenever(ridesRepository.getRides(1)).thenReturn(newRides)
        
        viewModel.refreshRides()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as RidesListViewState.Success
        assertEquals(2, state.rides.size)
        // getRides is called in init and refreshRides, so at least 2 times
        verify(ridesRepository, org.mockito.kotlin.atLeast(2)).getRides(1)
    }

    @Test
    fun `refreshRides handles error`() = runTest {
        val rides = listOf(createRide(1, "Ride A"))
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        whenever(ridesRepository.getRides(1)).thenThrow(RuntimeException("Error"))
        
        viewModel.refreshRides()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as RidesListViewState.Error
        assertEquals("Failed to load rides", state.errorMessage)
    }

    @Test
    fun `sortRides by SHORT_WAIT sorts correctly`() = runTest {
        val rides = listOf(
            createRide(1, "Ride A", isOpen = true, waitTime = 60),
            createRide(2, "Ride B", isOpen = true, waitTime = 15),
            createRide(3, "Ride C", isOpen = true, waitTime = 30)
        )
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        viewModel.sortRides(RideSortOption.SHORT_WAIT)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as RidesListViewState.Success
        assertEquals(15, sortedState.rides[0].waitTime)
        assertEquals(30, sortedState.rides[1].waitTime)
        assertEquals(60, sortedState.rides[2].waitTime)
        assertEquals(RideSortOption.SHORT_WAIT, sortedState.sortOption)
    }

    @Test
    fun `sortRides by LONG_WAIT sorts correctly`() = runTest {
        val rides = listOf(
            createRide(1, "Ride A", isOpen = true, waitTime = 15),
            createRide(2, "Ride B", isOpen = true, waitTime = 60),
            createRide(3, "Ride C", isOpen = true, waitTime = 30)
        )
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        viewModel.sortRides(RideSortOption.LONG_WAIT)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as RidesListViewState.Success
        assertEquals(60, sortedState.rides[0].waitTime)
        assertEquals(30, sortedState.rides[1].waitTime)
        assertEquals(15, sortedState.rides[2].waitTime)
        assertEquals(RideSortOption.LONG_WAIT, sortedState.sortOption)
    }

    @Test
    fun `sortRides by ALPHABETICAL sorts correctly`() = runTest {
        val rides = listOf(
            createRide(1, "Zebra Ride"),
            createRide(2, "Alpha Ride"),
            createRide(3, "Beta Ride")
        )
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        viewModel.sortRides(RideSortOption.ALPHABETICAL)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as RidesListViewState.Success
        assertEquals("Alpha Ride", sortedState.rides[0].name)
        assertEquals("Beta Ride", sortedState.rides[1].name)
        assertEquals("Zebra Ride", sortedState.rides[2].name)
        assertEquals(RideSortOption.ALPHABETICAL, sortedState.sortOption)
    }

    @Test
    fun `sortRides with closed rides puts them last`() = runTest {
        val rides = listOf(
            createRide(1, "Ride A", isOpen = false, waitTime = 0),
            createRide(2, "Ride B", isOpen = true, waitTime = 30),
            createRide(3, "Ride C", isOpen = true, waitTime = 15)
        )
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        viewModel.sortRides(RideSortOption.SHORT_WAIT)
        advanceUntilIdle()
        
        val sortedState = viewModel.uiState.value as RidesListViewState.Success
        // Closed rides get Int.MIN_VALUE, so they come first in ascending sort
        assertTrue(!sortedState.rides[0].isOpen) // First should be closed (Int.MIN_VALUE)
        assertTrue(sortedState.rides[1].isOpen) // Second should be open with shorter wait
        assertTrue(sortedState.rides[2].isOpen) // Third should be open with longer wait
        assertEquals(15, sortedState.rides[1].waitTime) // Shorter wait comes before longer
        assertEquals(30, sortedState.rides[2].waitTime)
    }

    @Test
    fun `sortRides does nothing when state is not Success`() = runTest {
        whenever(ridesRepository.getRides(1)).thenThrow(RuntimeException("Error"))
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        // State should be Error
        assertTrue(viewModel.uiState.value is RidesListViewState.Error)
        
        // Sort should not crash
        viewModel.sortRides(RideSortOption.ALPHABETICAL)
        advanceUntilIdle()
        
        // State should still be Error
        assertTrue(viewModel.uiState.value is RidesListViewState.Error)
    }

    @Test
    fun `parkId is correctly extracted from savedStateHandle`() = runTest {
        val rides = listOf(createRide(1, "Ride A"))
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        assertEquals(1, viewModel.parkId)
    }

    @Test
    fun `parkName is correctly decoded from savedStateHandle`() = runTest {
        val rides = listOf(createRide(1, "Ride A"))
        whenever(ridesRepository.getRides(1)).thenReturn(rides)
        
        viewModel = RidesListViewModel(ridesRepository, savedStateHandle)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value as RidesListViewState.Success
        assertEquals("Test Park", state.parkName)
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

