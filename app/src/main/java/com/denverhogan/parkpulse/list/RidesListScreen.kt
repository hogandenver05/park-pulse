package com.denverhogan.parkpulse.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.denverhogan.parkpulse.ui.theme.ParkPulseTheme
import com.denverhogan.parkpulse.model.Ride
import com.denverhogan.parkpulse.model.RidesListViewState

@Composable
fun RidesListScreen(
    modifier: Modifier = Modifier,
    onRideClick: (Ride) -> Unit,
    viewModel: RidesListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    when (val state = viewState) {
        is RidesListViewState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is RidesListViewState.Success -> {
            val rides = state.rides
            if (rides.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No rides available at the moment.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = modifier.fillMaxSize()
                ) {
                    items(rides) { ride ->
                        RidesListItem(ride = ride, onClick = { onRideClick(ride) })
                    }
                }
            }
        }

        is RidesListViewState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun RidesListItem(ride: Ride, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = ride.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (ride.isOpen) "${ride.waitTime} minute wait" else "Closed",
                fontSize = 14.sp,
                color = if (ride.isOpen)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RidesListScreenPreview() {
    ParkPulseTheme {
        RidesListScreen(onRideClick = { })
    }
}
