package com.denverhogan.parkpulse.model

sealed interface RidesListViewState {
    data object Loading : RidesListViewState
    data class Success(
        val rides: List<Ride>
    ) : RidesListViewState

    data class Error(
        val errorMessage: String
    ) : RidesListViewState
}
