package com.denverhogan.parkpulse.ui.rides

import com.denverhogan.parkpulse.model.Ride

sealed interface RideDetailsViewState {
    data object Loading : RideDetailsViewState
    data class Success(val ride: Ride) : RideDetailsViewState
    data class Error(val errorMessage: String) : RideDetailsViewState
}
