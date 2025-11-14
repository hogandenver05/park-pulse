package com.denverhogan.parkpulse.ui.rides

import com.denverhogan.parkpulse.model.Ride
import com.denverhogan.parkpulse.model.RideSortOption

sealed interface RidesListViewState {
    data object Loading : RidesListViewState
    data class Success(
        val parkName: String, 
        val rides: List<Ride>,
        val sortOption: RideSortOption = RideSortOption.LONG_WAIT
    ) : RidesListViewState

    data class Error(val errorMessage: String) : RidesListViewState
}
