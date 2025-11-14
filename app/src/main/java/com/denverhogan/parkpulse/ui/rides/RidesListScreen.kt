package com.denverhogan.parkpulse.ui.rides

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.denverhogan.parkpulse.model.Ride
import com.denverhogan.parkpulse.model.RideSortOption
import com.denverhogan.parkpulse.ui.theme.RideClosedGrey
import com.denverhogan.parkpulse.ui.theme.WaitTimeGreen
import com.denverhogan.parkpulse.ui.theme.WaitTimeRed
import com.denverhogan.parkpulse.ui.theme.WaitTimeYellow
import com.denverhogan.parkpulse.util.formatLastUpdated

@Composable
fun RidesListScreen(
    viewModel: RidesListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    when (val state = uiState) {
        is RidesListViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is RidesListViewState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Rides at ${state.parkName}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                SortButtons(
                    selectedOption = state.sortOption,
                    onSort = { viewModel.sortRides(it) }
                )
                RidesList(
                    rides = state.rides,
                    parkId = viewModel.parkId,
                    onRideClick = { url -> uriHandler.openUri(url) }
                )
            }
        }

        is RidesListViewState.Error -> {
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

@Composable
fun SortButtons(
    selectedOption: RideSortOption,
    onSort: (RideSortOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Sort By:")

        @Composable
        fun SortButton(option: RideSortOption, text: String) {
            Button(
                onClick = { onSort(option) },
                colors = if (selectedOption == option) {
                    ButtonDefaults.buttonColors()
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            ) {
                Text(text)
            }
        }

        SortButton(RideSortOption.LONG_WAIT, "Long Wait")
        SortButton(RideSortOption.SHORT_WAIT, "Short Wait")
        SortButton(RideSortOption.ALPHABETICAL, "A-Z")
    }
}

@Composable
fun RidesList(
    rides: List<Ride>,
    parkId: String,
    onRideClick: (String) -> Unit
) {
    if (rides.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No rides available at the moment.")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(rides) { ride ->
                val url = "https://queue-times.com/parks/$parkId/rides/${ride.id}"
                RidesListItem(ride = ride, onClick = { onRideClick(url) })
            }
        }
    }
}

@Composable
fun RidesListItem(
    ride: Ride,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = getWaitTimeColor(
                isOpen = ride.isOpen,
                waitTime = ride.waitTime
            )
        )
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
            Text(text = "Wait Time: ${if (ride.isOpen) "${ride.waitTime} minutes" else "Closed"}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Last Updated: ${formatLastUpdated(ride.lastUpdated)}")
        }
    }
}

@Composable
private fun getWaitTimeColor(isOpen: Boolean, waitTime: Int): Color {
    if (!isOpen) {
        return RideClosedGrey
    }

    val green = WaitTimeGreen
    val yellow = WaitTimeYellow
    val red = WaitTimeRed

    val maxWaitTime = 120f

    val fraction = (waitTime / maxWaitTime).coerceIn(0f, 1f)

    return when {
        fraction < 0.5f -> lerp(green, yellow, fraction * 2)
        else -> lerp(yellow, red, (fraction - 0.5f) * 2)
    }
}

@Preview
@Composable
fun RidesListItemPreviewOpen() {
    RidesListItem(
        ride = Ride(
            id = 1,
            name = "Space Mountain",
            isOpen = true,
            waitTime = 135,
            lastUpdated = "2025-10-27T10:00:00Z"
        ),
        onClick = {}
    )
}

@Preview
@Composable
fun RidesListItemPreviewClosed() {
    RidesListItem(
        ride = Ride(
            id = 2,
            name = "Haunted Mansion",
            isOpen = false,
            waitTime = 0,
            lastUpdated = "2035-11-14T10:05:00Z"
        ),
        onClick = {}
    )
}
