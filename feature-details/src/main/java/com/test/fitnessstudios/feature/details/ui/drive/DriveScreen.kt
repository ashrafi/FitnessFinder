package com.test.fitnessstudios.feature.details.ui.drive

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.test.fitnessstudios.feature.details.R
import com.test.fitnessstudios.feature.details.ui.LocationDetailsViewModel
import com.test.fitnessstudios.feature.details.ui.TAG

@Composable
fun DriveScreen(
    modifier: Modifier = Modifier,
    posLocation: LatLng?,
    driveDirPoints: List<LatLng>,
    viewModel: LocationDetailsViewModel = hiltViewModel()
) {
    Log.d(TAG, "DriveScreen: This is the loc $posLocation ")
    val cameraPositionState = rememberCameraPositionState {
        posLocation?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(posLocation.latitude, posLocation.longitude),
                15f
            )
        }
    }
    var isMapLoaded by remember { mutableStateOf(false) }

    var mapProperties by remember {
        mutableStateOf(viewModel.maProp)
    }

    var mapUiSettings by remember {
        mutableStateOf(viewModel.mapUI)
    }
    Column() {
        LocationPermissions()
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMapView(
                modifier = Modifier.matchParentSize(),
                mpp = mapProperties.value,
                mps = mapUiSettings.value,
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoaded = true
                },
                driveDirPoints = driveDirPoints
            )
            if (!isMapLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    mpp: MapProperties,
    mps: MapUiSettings,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    driveDirPoints: List<LatLng> = emptyList<LatLng>()
) {

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mpp,
        uiSettings = mps,
        onMapLoaded = onMapLoaded,
        onPOIClick = { poi ->
            Log.d("GraphQL", "POI clicked: ${poi.name}")
        }
    ) {
        if (driveDirPoints.isNotEmpty()) {
            Polyline(
                driveDirPoints,
                color = Color.Magenta
            )

            Marker(
                state = rememberMarkerState(position = driveDirPoints.first()),
                title = "Start",
                snippet = "",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )

            Marker(
                state = rememberMarkerState(position = driveDirPoints.last()),
                title = "End",
                snippet = "",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
        }
    }
}

@Preview
@Composable
fun DriveScreenPreview() {
    val posLocation = LatLng(37.4419, -122.1419)
    val driveDirPoints = listOf(
        LatLng(37.4419, -122.1419),
        LatLng(37.4419, -122.1419),
        LatLng(37.4419, -122.1419)
    )

    DriveScreen(
        modifier = Modifier.fillMaxSize(),
        posLocation = posLocation,
        driveDirPoints = driveDirPoints
    )
}


/*@Preview
@Composable
fun GoogleMapViewPreview() {
    GoogleMapView(
        cameraPositionState = rememberCameraPositionState(),
        driveDirPoints = listOf(
            LatLng(37.423060, -122.084270),
            LatLng(37.421160, -122.084270)
        )
    )
}*/

/**
 * Compose can not preview Google Maps
 */
@Preview
@Composable
fun GoogleMapViewPreviewLong() {
    val posLocation = LatLng(37.7749, -122.4194)
    val testPoints = remember {
        mutableStateListOf<LatLng>(
            LatLng(posLocation.latitude, posLocation.longitude),
            LatLng(
                posLocation.latitude + Math.random() / 100,
                posLocation.longitude + Math.random() / 100
            ),
            LatLng(
                posLocation.latitude + Math.random() / 100,
                posLocation.longitude + Math.random() / 100
            ),
            LatLng(
                posLocation.latitude + Math.random() / 100,
                posLocation.longitude + Math.random() / 100
            ),
            LatLng(
                posLocation.latitude + Math.random() / 100,
                posLocation.longitude + Math.random() / 100
            ),
            LatLng(
                posLocation.latitude + Math.random() / 100,
                posLocation.longitude + Math.random() / 100
            ),
            LatLng(posLocation.latitude, posLocation.longitude),
        )
    }
    //GoogleMapView()
}

// Tutorial
// https://www.youtube.com/watch?v=KDKF0b4cjF8
// https://lwgmnz.me/google-maps-and-directions-api-using-kotlin/