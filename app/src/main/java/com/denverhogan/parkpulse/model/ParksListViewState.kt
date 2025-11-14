package com.denverhogan.parkpulse.model

sealed interface ParksListViewState {
    data object Loading : ParksListViewState
    data class Success(
        val parks: List<Park>,
        val sortOption: SortOption = SortOption.DEFAULT
    ) : ParksListViewState

    data class Error(
        val errorMessage: String
    ) : ParksListViewState
}

enum class SortOption {
    DEFAULT,
    ALPHABETICAL,
    DISTANCE
}
