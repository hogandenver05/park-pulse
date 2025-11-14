package com.denverhogan.parkpulse.ui.rides

import com.denverhogan.parkpulse.model.Ride

sealed interface RidesListViewState {
    data object Loading : RidesListViewState
    data class Success(val rides: List<Ride>) : RidesListViewState
    data class Error(val errorMessage: String) : RidesListViewState
}
