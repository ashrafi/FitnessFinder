package com.test.fitnessstudios.feature.locations.ui.map

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.FitLocation
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState.Success
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel


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
    val locationState = viewModel.curLocation.collectAsState(initial = null)

    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 15f)
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
            viewModel.setCameraLocation(cameraLocation)
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
                items = (items as Success).launchList,
                mapProperties.value,
                mapUiSettings.value,
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
    cp: CameraPositionState
) {
    // MutableStateFlow
    Column {
        LocationPermissions()
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                properties = mpp,
                uiSettings = mps,
                cameraPositionState = cp,
            ) {
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
                                        icon = BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_CYAN
                                        ),
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
