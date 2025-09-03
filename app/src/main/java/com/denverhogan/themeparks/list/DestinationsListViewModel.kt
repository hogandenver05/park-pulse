package com.denverhogan.themeparks.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.themeparks.model.DestinationListItem
import com.denverhogan.themeparks.model.DestinationsListViewState
import com.denverhogan.themeparks.model.GetAllDestinationsResult
import com.denverhogan.themeparks.repository.DestinationsListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DestinationsListViewModel(private val repository: DestinationsListRepository) : ViewModel() {
    private val _viewState = MutableStateFlow<DestinationsListViewState>(DestinationsListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.update {
                val result = repository.getAllDestinations()
                when (result) {
                    is GetAllDestinationsResult.Error -> DestinationsListViewState.Error(message = result.message)
                    is GetAllDestinationsResult.Success -> DestinationsListViewState.Success(destinations = result.destinations)
                }
            }
        }
    }
}