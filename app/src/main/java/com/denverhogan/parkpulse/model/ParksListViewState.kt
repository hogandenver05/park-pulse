package com.denverhogan.parkpulse.model

sealed interface ParksListViewState {
    data object Loading : ParksListViewState
    data class Success(
        val parks: List<Park>
    ) : ParksListViewState

    data class Error(
        val errorMessage: String
    ) : ParksListViewState
}
