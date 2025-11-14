package com.denverhogan.parkpulse.ui.rides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RideDetailsScreen(
    viewModel: RideDetailsViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    when (val state = viewState) {
        is RideDetailsViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is RideDetailsViewState.Success -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Text(text = state.ride.name, style = MaterialTheme.typography.headlineMedium)
            }
        }

        is RideDetailsViewState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
