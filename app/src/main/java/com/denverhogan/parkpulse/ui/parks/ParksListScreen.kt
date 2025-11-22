package com.denverhogan.parkpulse.ui.parks

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.denverhogan.parkpulse.R
import com.denverhogan.parkpulse.model.Park
import com.denverhogan.parkpulse.model.ParkSortOption
import com.denverhogan.parkpulse.ui.theme.VibrantRed
import java.net.URLEncoder

private const val KM_TO_MILES = 0.621371

@Composable
fun ParksListScreen(
    navController: NavController,
    viewModel: ParksListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    when (val state = uiState) {
        is ParksListViewState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Park Pulse Logo",
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }
        }

        is ParksListViewState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { uriHandler.openUri("https://github.com/hogandenver05/park-pulse/blob/main/README.md") },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.graphic),
                            contentDescription = "Park Pulse - Tap to view GitHub repository",
                            modifier = Modifier
                                .height(32.dp),
                            contentScale = ContentScale.FillHeight
                        )
                        Image(
                            painter = painterResource(id = R.drawable.text),
                            contentDescription = "Park Pulse - Tap to view GitHub repository",
                            modifier = Modifier
                                .height(32.dp),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                    IconButton(onClick = { viewModel.refreshParks() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
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
                    contentDescription = "Favorite",
                    tint = if (park.isFavorite) VibrantRed else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
