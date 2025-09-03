package com.denverhogan.themeparks.model

sealed interface DestinationsListViewState {
    data object Loading : DestinationsListViewState
    data class Success(
        val destinations: List<DestinationListItem>
    ) : DestinationsListViewState

    data class Error(
        val message: String
    ) : DestinationsListViewState
}