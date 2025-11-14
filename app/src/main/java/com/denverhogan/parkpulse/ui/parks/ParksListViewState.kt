package com.denverhogan.parkpulse.ui.parks

import com.denverhogan.parkpulse.model.Park

sealed interface ParksListViewState {
    data object Loading : ParksListViewState
    data class Success(val parks: List<Park>) : ParksListViewState
    data class Error(val errorMessage: String) : ParksListViewState
}
