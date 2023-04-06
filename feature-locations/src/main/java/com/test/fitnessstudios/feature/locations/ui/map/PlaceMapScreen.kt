package com.test.fitnessstudios.feature.locations.ui.map

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.YelpCategory
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel
import com.test.fitnessstudios.feature.locations.ui.TAG


/*
Example:
 */
@Composable
fun PlaceMapScreen(
    modifier: Modifier = Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val items by produceState<StudioLocationUiState>(
        initialValue = StudioLocationUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    val loc = viewModel.locationStateFlow.collectAsState()

    Log.d(TAG, "PlaceMapScreen: 1 This is the Lat / Lan $loc")

    // Set the initial position of the camera
    val cameraPositionState = rememberCameraPositionState {
        loc?.value?.let {
            position = CameraPosition(LatLng(it.latitude, it.longitude), 15F, 0F, 0F)
        }
        Log.d(TAG, "PlaceMapScreen: 2 This is the Lat / Lan $loc")
    }

    LaunchedEffect(loc.value) {
        loc?.value?.let {
            cameraPositionState.position =
                CameraPosition(LatLng(it.latitude, it.longitude), 15F, 0F, 0F)
        }
        Log.d(TAG, "PlaceMapScreen: 2 This is the Lat / Lan $loc")
    }

    // LaunchedEffect: run suspend functions in the scope of a composable //
    LaunchedEffect(cameraPositionState.isMoving) {
        val position = cameraPositionState.position
        val isMoving = cameraPositionState.isMoving

        if (!isMoving) {
            viewModel.setZoomLevel(cameraPositionState.position.zoom)
            viewModel.callYelpAPI(position.target)
            viewModel.currentCameraPosition = position.target
        }
    }
    // 37.7749° N, 122.4194° W
    // Set properties using MapProperties which you can use to recompose the map
    var mapProperties by remember {
        mutableStateOf(viewModel.maProp)
    }

    var mapUiSettings by remember {
        mutableStateOf(viewModel.mapUI)
    }



    when (items) {
        is StudioLocationUiState.Loading -> {
            // Show loading spinner
            Text(items.toString())
            Text("Showing Spinner")
        }

        is StudioLocationUiState.Success -> {

            PlaceMapScreen(
                modifier = modifier,
                items = (items as StudioLocationUiState.Success).launchList,
                mapProperties.value,
                mapUiSettings.value,
                viewModel.currentCategory,
                cameraPositionState
            )

        }

        is StudioLocationUiState.Error -> {
            // Show error message
            Text(items.toString())
        }

    }
}

@Composable
internal fun PlaceMapScreen(
    modifier: Modifier = Modifier,
    items: List<BusinessInfo?>?,
    mpp: MapProperties,
    mps: MapUiSettings,
    cat: String,
    cp: CameraPositionState
) {
    // MutableStateFlow
    Column {
        LocationPermissions()
        CollapsibleView()
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                properties = mpp,
                uiSettings = mps,
                cameraPositionState = cp,
            ) {
                var markCat =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                when (cat) {
                    YelpCategory.bars.name -> {
                        markCat =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                    }
                    YelpCategory.food.name -> {
                        markCat =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
                    }
                    YelpCategory.fitness.name -> {
                        markCat =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)
                    }
                }
                // viewModel.state.parkingSpots.forEach
                val context = LocalContext.current
                items?.forEach {
                    it?.let { business ->
                        it.let { business ->
                            business.coordinates?.let { place ->
                                if (place.latitude != null && place.longitude != null) {
                                    Marker(
                                        state = MarkerState(
                                            position = LatLng(
                                                place.latitude!!,
                                                place.longitude!!
                                            )
                                        ),
                                        title = "${it.name}",
                                        draggable = false,
                                        icon = markCat,
                                        alpha = (0.8f),
                                        flat = (true),
                                        zIndex = (1.0f),
                                        onClick = {
                                            business.rating?.let { rating ->
                                                if (rating > 0.0) {
                                                    Toast.makeText(
                                                        context,
                                                        "Rating: ${business.rating}\n",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            false // return true to indicate that the event has been handled
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun CollapsibleView(
    modifier: Modifier = Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val carrotIcon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown

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
                text = "Category ",
                modifier = Modifier.padding(start = 16.dp),
                //style = MaterialTheme.typography.subtitle1
            )
        }

        if (expanded) {
            Column(modifier = modifier) {
                Row {
                    Button(onClick = {
                        viewModel.currentCategory = YelpCategory.food.name
                        viewModel.callYelpAPI()
                    }) {
                        Text("Food")
                    }
                    Button(
                        onClick = {
                            viewModel.currentCategory = YelpCategory.fitness.name
                            viewModel.callYelpAPI()
                        }
                    ) {
                        Text("Fitness")
                    }
                    Button(onClick = {
                        viewModel.currentCategory = YelpCategory.bars.name
                        viewModel.callYelpAPI()
                    }) {
                        Text("Bars")
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


/*Column {
Button(onClick = {
    mapProperties = mapProperties.copy(
        isBuildingEnabled = !mapProperties.isBuildingEnabled
    )
}) {
    Text(text = "Toggle isBuildingEnabled")
}
Button(onClick = {
    mapUiSettings = mapUiSettings.copy(
        mapToolbarEnabled = !mapUiSettings.mapToolbarEnabled
    )
}) {
    Text(text = "Toggle mapToolbarEnabled")
}
}*/
