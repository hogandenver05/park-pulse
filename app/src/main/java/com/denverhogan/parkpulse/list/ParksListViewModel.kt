package com.denverhogan.parkpulse.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.model.ParksListViewState
import com.denverhogan.parkpulse.network.GetAllParksResult
import com.denverhogan.parkpulse.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParksListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _viewState = MutableStateFlow<ParksListViewState>(ParksListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.update {
                val result = repository.getAllParks()
                when (result) {
                    is GetAllParksResult.Success -> ParksListViewState.Success(parks = result.parks)
                    is GetAllParksResult.Error -> ParksListViewState.Error(errorMessage = result.message)
                }
            }
        }
    }
}
