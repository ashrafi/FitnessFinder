package com.test.fitnessstudios.feature.details.ui.drive

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.test.fitnessstudios.feature.details.ui.LocationDetailsViewModel
import com.test.fitnessstudios.feature.details.ui.TAG

@Composable
fun DriveScreen(
        modifier: Modifier = Modifier,
        viewModel: LocationDetailsViewModel = hiltViewModel(),
    ) {

    val currLoc = viewModel.currUserLoc.collectAsState(null).value
    var busLoc = viewModel.buslocation.value

    // Map preferences
    val mpp = viewModel.maProp
    val mui = viewModel.mapUI

    // When current location chages update the driving directions.
    LaunchedEffect(currLoc, mpp, mui) {
        Log.d(TAG, "LaunchedEffect -- Drive Screen the  currLoc is $currLoc & $busLoc")
        if ((currLoc != null) && (busLoc != null)) {
            currLoc?.let {
                viewModel.updateDrivePts(
                    userLoc = LatLng(it.latitude, it.longitude),
                    busiLoc = busLoc
                )
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        busLoc?.let {place ->
            position = CameraPosition.fromLatLngZoom(
                LatLng(place.latitude, place.longitude),
                15f
            )
        }
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    Column() {
        LocationPermissions()
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMapView(
                modifier = modifier.matchParentSize(),
                mpp = mpp.value,
                mui = mui.value,
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoaded = true
                }
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
    mui: MapUiSettings,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    viewModel: LocationDetailsViewModel = hiltViewModel(),
    ) {
    var driveDirPoints = viewModel.drivingPoints.value
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mpp,
        uiSettings = mui,
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

    val mapUI =
        MapUiSettings(
            myLocationButtonEnabled = false,
            mapToolbarEnabled = true
        )


    val maProp =
        MapProperties(
            isMyLocationEnabled = false, // viewModel.test.value,
            maxZoomPreference = 15f,
            minZoomPreference = 10f
        )


    DriveScreen(
        modifier = Modifier.fillMaxSize(),
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