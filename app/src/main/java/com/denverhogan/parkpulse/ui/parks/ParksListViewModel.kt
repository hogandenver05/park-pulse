package com.denverhogan.parkpulse.ui.parks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.data.ParkRepository
import com.denverhogan.parkpulse.model.ParkSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParksListViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ParksListViewState>(ParksListViewState.Loading)
    val uiState: StateFlow<ParksListViewState> = _uiState.asStateFlow()

    init {
        getParks()
    }

    private fun getParks() {
        viewModelScope.launch {
            _uiState.value = ParksListViewState.Loading
            try {
                val parks = parkRepository.getParks()
                _uiState.value = ParksListViewState.Success(parks)
                sortParks(ParkSortOption.FAVORITES)
            } catch (e: Exception) {
                _uiState.value = ParksListViewState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun sortParks(sortOption: ParkSortOption) {
        if (_uiState.value is ParksListViewState.Success) {
            val currentState = _uiState.value as ParksListViewState.Success
            val sortedParks = when (sortOption) {
                ParkSortOption.FAVORITES -> currentState.parks.sortedWith(compareByDescending { it.isFavorite })
                ParkSortOption.ALPHABETICAL -> currentState.parks.sortedWith(compareBy { it.name })
                ParkSortOption.DISTANCE -> currentState.parks.sortedWith(compareBy { it.distance })
            }
            _uiState.update {
                (it as ParksListViewState.Success).copy(
                    parks = sortedParks,
                    sortOption = sortOption
                )
            }
        }
    }

    fun toggleFavorite(parkId: Int) {
        viewModelScope.launch {
            if (_uiState.value is ParksListViewState.Success) {
                val currentState = _uiState.value as ParksListViewState.Success
                val park = currentState.parks.find { it.id == parkId }
                if (park != null) {
                    if (park.isFavorite) {
                        parkRepository.removeFavorite(parkId)
                    } else {
                        parkRepository.addFavorite(parkId)
                    }
                    val updatedParks = parkRepository.getParks()
                    _uiState.update {
                        (it as ParksListViewState.Success).copy(
                            parks = updatedParks,
                        )
                    }
                    sortParks(currentState.sortOption)
                }
            }
        }
    }
}
