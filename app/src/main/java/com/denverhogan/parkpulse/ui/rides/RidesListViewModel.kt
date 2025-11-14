package com.denverhogan.parkpulse.ui.rides

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denverhogan.parkpulse.repository.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class RidesListViewModel @Inject constructor(
    private val ridesRepository: RidesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState = MutableStateFlow<RidesListViewState>(RidesListViewState.Loading)
    val viewState: StateFlow<RidesListViewState> = _viewState

    val parkId: String = savedStateHandle.get<String>("parkId")!!
    private val parkName: String = URLDecoder.decode(savedStateHandle.get<String>("parkName")!!, "UTF-8")

    init {
        viewModelScope.launch {
            try {
                _viewState.value = RidesListViewState.Success(
                    parkName = parkName,
                    rides = ridesRepository.getRides(parkId.toInt())
                )
            } catch (_: Exception) {
                _viewState.value = RidesListViewState.Error("Failed to load rides")
            }
        }
    }
}
