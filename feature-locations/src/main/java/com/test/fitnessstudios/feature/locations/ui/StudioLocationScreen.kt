package com.test.fitnessstudios.feature.locations.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.database.FitnessStudio

//@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StudioLocationScreenNav(
    navToDetails: NavHostController,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    var myFavs: List<FitnessStudio> = emptyList()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val placeHolder = LatLng(37.7749, -122.4194)

    /**
     * collectAsStateWithLifecycle is a composable function that collects values from a flow and
     * represents the latest value as Compose State in a lifecycle-aware manner.
     */
    // collectAsState will turn our Flow into state that can be consumed by Composables
    val state = viewModel.uiState.collectAsState(initial = StudioLocationUiState.Loading)

    Column() {
        CollapsibleView()
        when (val value = state.value) {
            is StudioLocationUiState.Success -> LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 40.dp)
            ) {
                items(value.launchList ?: emptyList()) {
                    Text(it?.name.toString())
                    Text("ID for this is ${it?.id}", fontSize = 12.sp)
                    Row {
                        Button(onClick = {
                            navToDetails.navigate("details/${it?.id}")
                        }) {
                            Text("Details")
                        }
                        it?.id.let { busID ->
                            FavoriteButton(
                                fav = myFavs.contains(myFavs.find { favList ->
                                    favList.uid == busID
                                }),
                                //fav = myFavs.forEach.contains(it?.id)
                                add = {
                                    Log.d(TAG, "StudioLocationScreenNav: add to favorites")
                                },
                                del = {
                                    Log.d(TAG, "StudioLocationScreenNav: remove from favorites")
                                }
                            )
                        }
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
    fav: Boolean,
) {
    IconToggleButton(
        checked = fav,
        onCheckedChange = {
            if (!fav)
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
            imageVector = if (fav) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}

@Composable
fun CollapsibleView(
    modifier: Modifier = Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val carrotIcon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown
    val placeHolder = LatLng(37.7749, -122.4194)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Icon(
                imageVector = carrotIcon,
                contentDescription = null,
                //tint = MaterialTheme.colors.onSurface
            )
            Text(
                text = "Toggle Collapsible View",
                modifier = Modifier.padding(start = 16.dp),
                //style = MaterialTheme.typography.subtitle1
            )
        }

        if (expanded) {
            Column(modifier = modifier) {
                Row {
                    Button(onClick = { viewModel.callYelpAPI("food", placeHolder) }) {
                        Text("Food")
                    }
                    Button(onClick = { viewModel.callYelpAPI("fitness", placeHolder) }) {
                        Text("Fitness")
                    }
                    Button(onClick = { viewModel.callYelpAPI("bars", placeHolder) }) {
                        Text("Nothing")
                    }
                }

            }

            /*if (items_test is StudioLocationUiState.SuccessFitness) {
                myFavs = (items_test as StudioLocationUiState.SuccessFitness).data

                myFavs.forEach {
                    Text("Saved item: $it")
                }
            }*/
        }
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