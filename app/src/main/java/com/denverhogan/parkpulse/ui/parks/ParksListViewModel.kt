package com.denverhogan.parkpulse.ui.parks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.repository.ParksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParksListViewModel @Inject constructor(
    private val parksRepository: ParksRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ParksListViewState>(ParksListViewState.Loading)
    val viewState: StateFlow<ParksListViewState> = _viewState

    init {
        viewModelScope.launch {
            try {
                _viewState.value = ParksListViewState.Success(parksRepository.getParks())
            } catch (_: Exception) {
                _viewState.value = ParksListViewState.Error("Failed to load parks")
            }
        }
    }
}
