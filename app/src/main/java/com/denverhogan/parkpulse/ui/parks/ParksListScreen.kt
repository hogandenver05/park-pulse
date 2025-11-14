package com.denverhogan.parkpulse.ui.parks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.denverhogan.parkpulse.model.ParksListViewState
import com.denverhogan.parkpulse.model.SortOption
import java.net.URLEncoder

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
                SortButtons(onSort = { viewModel.sortParks(it) })
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
                Text(text = state.errorMessage)
            }
        }
    }
}

@Composable
fun SortButtons(onSort: (SortOption) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Sort By:")
        Button(onClick = { onSort(SortOption.DEFAULT) }) {
            Text("Default")
        }
        Button(onClick = { onSort(SortOption.DISTANCE) }) {
            Text("Distance")
        }
        Button(onClick = { onSort(SortOption.ALPHABETICAL) }) {
            Text("A-Z")
        }
    }
}

@Composable
fun ParksList(
    parks: List<Park>,
    onFavorite: (Int) -> Unit,
    onParkClick: (park: Park) -> Unit
) {
    LazyColumn {
        items(parks) { park ->
            ParkCard(
                park = park,
                onFavorite = { onFavorite(park.id) },
                onParkClick = { onParkClick(park) }
            )
        }
    }
}

@Composable
fun ParkCard(
    park: Park,
    onFavorite: () -> Unit,
    onParkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onParkClick)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = park.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = park.country)
                park.distance?.let {
                    Text(text = "%.2f miles away".format(it * 0.621371))
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
