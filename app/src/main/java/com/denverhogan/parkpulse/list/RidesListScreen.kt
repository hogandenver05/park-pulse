package com.denverhogan.parkpulse.list

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.denverhogan.parkpulse.ui.theme.ParkPulseTheme
import com.denverhogan.themeparks.model.Ride
import com.denverhogan.themeparks.model.RidesListViewState
import org.w3c.dom.Text

@Composable
fun RidesListScreen(
    modifier: Modifier, onRideClick: (Ride) -> Unit, viewModel: RidesListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    when (viewState) {
        is RidesListViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is RidesListViewState.Success -> {
            val rides = (viewState as RidesListViewState.Success).rides
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(rides) { ride ->
                    RidesListItem(ride = ride, onClick = { onRideClick(ride) })
                }
            }
        }

        is RidesListViewState.Error -> {
            val message = (viewState as RidesListViewState.Error).errorMessage
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message)
            }
        }
    }

}

@Composable
fun RidesListItem(ride: Ride, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .clickable(onClick = onClick)

    ) {
        Text(
            text = ride.name,
            fontSize = 20.sp,
        )
        Text(
            text = if (ride.isOpen) "${ride.waitTime} minute wait" else "Closed",
            fontSize = 15.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RidesListScreenPreview() {
    ParkPulseTheme {
        RidesListScreen(
            onRideClick = {},
            modifier = Modifier,
        )
    }
}
