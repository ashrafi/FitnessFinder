package com.test.fitnessstudios.feature.locations.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.FitLocation
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState.Success
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel


/*
Example:
 */
@Composable
fun PlaceMap(
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
    if (items is Success) {
        PlaceMap(
            items = (items as Success).launchList,
            viewModel = viewModel,
            modifier = modifier
        )

    }
}

@Composable
internal fun PlaceMap(
    items: List<BusinessInfo?>?,
    viewModel: StudioLocationViewModel,
    modifier: Modifier = Modifier
) {

    // MutableStateFlow
    val posLocation by viewModel.curLocation.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(posLocation.latitude, posLocation.longitude), 15f)
    }

    /*LaunchedEffect(posLocation) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(LatLng(posLocation.latitude, posLocation.longitude), 15f))
    }*/

    // LaunchedEffect: run suspend functions in the scope of a composable //
    LaunchedEffect(cameraPositionState.isMoving) {
        val position = cameraPositionState.position
        val isMoving = cameraPositionState.isMoving

        if (!isMoving) {
            val cameraLocation = FitLocation(position.target.latitude, position.target.longitude)
            viewModel.setCameraPosition(cameraLocation)
            viewModel.setZoomLevel(cameraPositionState.position.zoom)
            viewModel.setLocation(cameraLocation)
        }
    }
    // 37.7749° N, 122.4194° W
    // Set properties using MapProperties which you can use to recompose the map
    var mapProperties by remember { mutableStateOf(viewModel.maProp) }

    var mapUiSettings by remember {
        mutableStateOf(
            viewModel.mapUI
        )
    }

    Column {
        Text("This is the location size: ${viewModel.markers}")
        LocationPermissions()
        Button(onClick = { viewModel.updateLocTest() }) {
            Text("Move Marker")
        }
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                properties = mapProperties.collectAsState().value,
                uiSettings = mapUiSettings.collectAsState().value,
                cameraPositionState = cameraPositionState,
            ) {

                // viewModel.state.parkingSpots.forEach
                viewModel.markers.forEach {
                    Marker(
                        state = MarkerState(
                            position = it
                        ),
                        title = "Marker from list"
                    )
                }

                items?.forEach {
                    it?.let { business ->
                        it?.let { business ->
                            business.coordinates?.let { place ->
                                if (place.latitude != null && place.longitude != null) {
                                    Marker(
                                        state = MarkerState(
                                            position = LatLng(
                                                place.latitude!!,
                                                place.longitude!!
                                            )
                                        ),
                                        title = "${it.name}\n${it.rating}",
                                        draggable = true,
                                        icon = BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                                }
                            }
                        }
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
        }

    }
}
