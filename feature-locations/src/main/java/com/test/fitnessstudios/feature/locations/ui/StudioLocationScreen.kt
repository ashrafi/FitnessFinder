package com.test.fitnessstudios.feature.locations.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage


//@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StudioLocationScreen(
    modifier: Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {

    /**
     * collectAsStateWithLifecycle is a composable function that collects values from a flow and
     * represents the latest value as Compose State in a lifecycle-aware manner.
     */
    // collectAsState will turn our Flow into state that can be consumed by Composables
    val state = viewModel.uiState.collectAsState(initial = UiState.Loading)

    Column(modifier = modifier) {
        Row {
            Button(onClick = { viewModel.callYelpAPI("food") }) {
                Text("Food")
            }
            Button(onClick = { viewModel.callYelpAPI("fitness") }) {
                Text("Fitness")
            }
            Button(onClick = { viewModel.callYelpAPI("bars") }) {
                Text("Nothing")
            }
        }

        when (val value = state.value) {
            is UiState.Success -> LazyColumn {
                items(value.launchList ?: emptyList()) {
                    Text(it?.name.toString())
                    Text(it?.id.toString())
                    Log.d("GraphQL", it?.id.toString())
                    Text(it?.fav.toString())
                    FavoriteButton(id = { viewModel.add(it?.id.toString()) })
                    AsyncImage(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        model = it?.photos?.first(), contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    //Log.d("GraphQL", "this is it ${it.name}")
                }
            }
            else -> {
                Text("No Items found")
            }
        }
    }
}


@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63),
    id: () -> Unit
) {

    var isFavorite by remember { mutableStateOf(false) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            if (isFavorite) {
                id()
            }
        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}