package com.test.fitnessstudios.feature.locations.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

//@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StudioLocationScreenNav(
    navToDetails: NavHostController,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    /**
     * collectAsStateWithLifecycle -- is a composable function that collects values from a flow and
     * represents the latest value as Compose State in a lifecycle-aware manner.
     *
     * collectAsState -- will turn our Flow into state that can be consumed by Composables
     */
    val state = viewModel.uiState.collectAsState(initial = StudioLocationUiState.Loading)

    Column() {
        when (val value = state.value) {
            is StudioLocationUiState.Success -> LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 40.dp)
            ) {
                items(value.launchList ?: emptyList()) { busInfo ->
                    Text("ID for this is ${busInfo?.id}", fontSize = 12.sp)
                    Row {
                        Text(busInfo?.name.toString())
                        busInfo?.id?.let { busID ->
                            FavoriteButton(
                                fav = viewModel.containsFav(busID),
                                add = {
                                    viewModel.addFavBus(busInfo)
                                    Log.d(TAG, "StudioLocationScreenNav: add to favorites")
                                },
                                del = {
                                    viewModel.delFavBus(busInfo)
                                    Log.d(TAG, "StudioLocationScreenNav: remove from favorites")
                                }
                            )
                        }
                    }
                    Button(onClick = {
                        navToDetails.navigate("details/${busInfo?.id}")
                    }) {
                        Text("Details")
                    }
                    DrawLine(
                        modifier = Modifier.padding(12.dp)
                    )
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
    add: () -> Unit,
    del: () -> Unit,
    fav: Flow<Boolean?>,
) {
    val isFav = fav.collectAsState(null).value ?: false
    IconToggleButton(
        checked = isFav,
        onCheckedChange = {
            if (!isFav)
                add()
            else
                del()
        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFav) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}

@Composable
fun DrawLine(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize()) {
        drawLine(
            color = Color.Blue, // Color of the line
            start = Offset(x = 0f, y = size.height / 2), // Starting point of the line
            end = Offset(x = size.width, y = size.height / 2), // Ending point of the line
            strokeWidth = 4f // Width of the line
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteButtonPreview() {
    val isFav = remember { mutableStateOf(false) }
    FavoriteButton(
        add = { isFav.value = true },
        del = { isFav.value = false },
        fav = remember { flowOf(isFav.value) }
    )
}