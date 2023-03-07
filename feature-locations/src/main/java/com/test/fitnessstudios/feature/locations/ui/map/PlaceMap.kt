package com.test.fitnessstudios.feature.locations.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel


/*
Example:
 */
@Composable
fun PlaceMap(
    viewModel: StudioLocationViewModel = hiltViewModel()
) {

    var mapUiSettings by remember {
        mutableStateOf(
            viewModel.mapUI
        )
    }

    val posLocation by viewModel.location.collectAsState()


    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(posLocation.latitude, posLocation.longitude), 100f)
    }

    val checkedState = remember { mutableStateOf(false) }


    // Set properties using MapProperties which you can use to recompose the map
    var mapProperties by remember { mutableStateOf(viewModel.maProp) }
    Column {
        LocationPermissions()
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                properties = mapProperties.collectAsState().value,
                uiSettings = mapUiSettings.collectAsState().value,
                cameraPositionState = cameraPositionState,
            )

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
