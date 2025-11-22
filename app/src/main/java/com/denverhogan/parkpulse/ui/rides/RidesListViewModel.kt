package com.denverhogan.parkpulse.ui.rides

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.model.RideSortOption
import com.denverhogan.parkpulse.repository.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class RidesListViewModel @Inject constructor(
    private val ridesRepository: RidesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<RidesListViewState>(RidesListViewState.Loading)
    val uiState: StateFlow<RidesListViewState> = _uiState.asStateFlow()

    val parkId: Int = savedStateHandle.get<Int>("parkId")!!
    private val parkName: String = URLDecoder.decode(savedStateHandle.get<String>("parkName")!!, "UTF-8")

    init {
        viewModelScope.launch {
            try {
                val rides = ridesRepository.getRides(parkId)
                _uiState.value = RidesListViewState.Success(
                    parkName = parkName,
                    rides = rides
                )
                sortRides(RideSortOption.LONG_WAIT)
            } catch (_: Exception) {
                _uiState.value = RidesListViewState.Error("Failed to load rides")
            }
        }
    }

    fun refreshRides() {
        viewModelScope.launch {
            _uiState.value = RidesListViewState.Loading
            try {
                val rides = ridesRepository.getRides(parkId)
                _uiState.value = RidesListViewState.Success(
                    parkName = parkName,
                    rides = rides
                )
                sortRides(RideSortOption.LONG_WAIT)
            } catch (_: Exception) {
                _uiState.value = RidesListViewState.Error("Failed to load rides")
            }
        }
    }

    fun sortRides(sortOption: RideSortOption) {
        if (_uiState.value is RidesListViewState.Success) {
            val currentState = _uiState.value as RidesListViewState.Success
            val sortedRides = when (sortOption) {
                RideSortOption.SHORT_WAIT -> currentState.rides.sortedWith(
                    compareBy { ride ->
                        if (ride.isOpen) ride.waitTime else Int.MIN_VALUE
                    }
                )
                RideSortOption.LONG_WAIT -> currentState.rides.sortedWith(
                    compareByDescending { ride ->
                        if (ride.isOpen) ride.waitTime else Int.MIN_VALUE
                    }
                )
                RideSortOption.ALPHABETICAL -> currentState.rides.sortedBy { it.name }
            }
            _uiState.update {
                (it as RidesListViewState.Success).copy(
                    rides = sortedRides,
                    sortOption = sortOption
                )
            }
        }
    }
}
