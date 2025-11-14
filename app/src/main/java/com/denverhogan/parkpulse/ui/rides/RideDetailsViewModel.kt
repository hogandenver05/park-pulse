package com.denverhogan.parkpulse.ui.rides

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.repository.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideDetailsViewModel @Inject constructor(
    private val ridesRepository: RidesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState = MutableStateFlow<RideDetailsViewState>(RideDetailsViewState.Loading)
    val viewState: StateFlow<RideDetailsViewState> = _viewState

    private val parkId: String = savedStateHandle.get<String>("parkId")!!
    private val rideId: String = savedStateHandle.get<String>("rideId")!!

    init {
        viewModelScope.launch {
            try {
                val ride = ridesRepository.getRide(parkId.toInt(), rideId.toInt())
                if (ride != null) {
                    _viewState.value = RideDetailsViewState.Success(ride)
                } else {
                    _viewState.value = RideDetailsViewState.Error("Ride not found")
                }
            } catch (e: Exception) {
                _viewState.value = RideDetailsViewState.Error("Failed to load ride details")
            }
        }
    }
}