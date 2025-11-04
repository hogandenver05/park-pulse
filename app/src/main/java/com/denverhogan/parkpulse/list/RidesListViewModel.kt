package com.denverhogan.parkpulse.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.model.RidesListViewState
import com.denverhogan.parkpulse.network.GetAllRidesResult
import com.denverhogan.parkpulse.repository.RidesListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RidesListViewModel @Inject constructor(
    private val repository: RidesListRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<RidesListViewState>(RidesListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.update {
                val result = repository.getAllRides()
                when (result) {
                    is GetAllRidesResult.Success -> RidesListViewState.Success(rides = result.rides)
                    is GetAllRidesResult.Error -> RidesListViewState.Error(errorMessage = result.message)
                }
            }
        }
    }
}