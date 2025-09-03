package com.denverhogan.themeparks.model

sealed interface GetAllDestinationsResult {
    data class Success(val destinations: List<DestinationListItem>) : GetAllDestinationsResult
    data class Error(val message: String) : GetAllDestinationsResult
}