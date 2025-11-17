package com.denverhogan.parkpulse.ui.parks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.model.ParkSortOption
import java.net.URLEncoder

private const val KM_TO_MILES = 0.621371

@Composable
fun ParksListScreen(
    navController: NavController,
    viewModel: ParksListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ParksListViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ParksListViewState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Park Pulse",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                SortButtons(
                    selectedOption = state.sortOption,
                    onSort = { viewModel.sortParks(it) }
                )
                ParksList(
                    parks = state.parks,
                    onFavorite = { viewModel.toggleFavorite(it) },
                    onParkClick = { park ->
                        val encodedParkName = URLEncoder.encode(park.name, "UTF-8")
                        navController.navigate("parks/${park.id}/rides/$encodedParkName")
                    }
                )
            }
        }

        is ParksListViewState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun SortButtons(
    selectedOption: ParkSortOption,
    onSort: (ParkSortOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Sort By:")

        @Composable
        fun SortButton(option: ParkSortOption, text: String) {
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

        SortButton(ParkSortOption.FAVORITES, "Favorites")
        SortButton(ParkSortOption.DISTANCE, "Distance")
        SortButton(ParkSortOption.ALPHABETICAL, "A-Z")
    }
}

@Composable
fun ParksList(
    parks: List<Park>,
    onFavorite: (Int) -> Unit,
    onParkClick: (park: Park) -> Unit
) {
    if (parks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No rides available at the moment.")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(parks) { park ->
                ParksListItem(
                    park = park,
                    onFavorite = { onFavorite(park.id) },
                    onClick = { onParkClick(park) }
                )
            }
        }
    }
}

@Composable
fun ParksListItem(
    park: Park,
    onFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = park.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = park.country)
                park.distance?.let {
                    Text(text = "%.2f miles away".format(it * KM_TO_MILES))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = if (park.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}
