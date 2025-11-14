package com.denverhogan.parkpulse.ui.parks

import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.model.ParkSortOption

sealed interface ParksListViewState {
    data object Loading : ParksListViewState
    data class Success(
        val parks: List<Park>,
        val sortOption: ParkSortOption = ParkSortOption.FAVORITES
    ) : ParksListViewState

    data class Error(val errorMessage: String) : ParksListViewState
}
