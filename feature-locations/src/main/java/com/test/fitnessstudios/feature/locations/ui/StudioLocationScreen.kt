package com.test.fitnessstudios.feature.locations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
    //val state = viewModel.get().toFlow().collectAsState(initial = UiState.Loading)
    // val feedState by viewModel.feedUiState.collectAsStateWithLifecycle()

    // collectAsState will turn our Flow into state that can be consumed by Composables
    val state = viewModel.uiState.collectAsState(initial = UiState.Loading)

    Column(modifier = modifier) {
        Row() {
            Button(onClick = { viewModel.callYelpAPI("food") }) {
                Text("Food")
            }
            Button(onClick = { viewModel.callYelpAPI("fitness") }) {
                Text("Fitness")
            }
        }

        when (val value = state.value) {
            is UiState.Success -> LazyColumn(content = {
                items(value.launchList) {
                    Text(it.name.toString())
                    AsyncImage(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        model = it.photos?.first(), contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    //Log.d("GraphQL", "this is it ${it.name}")
                }
            })
            else -> {}
        }
    }
}