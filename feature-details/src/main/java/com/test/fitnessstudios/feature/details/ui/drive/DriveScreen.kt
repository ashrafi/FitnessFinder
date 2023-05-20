package com.test.fitnessstudios.feature.details.ui.drive

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import com.test.fitnessstudios.feature.details.ui.TAG

@Composable
fun DriveScreen(
    modifier: Modifier = Modifier,
    posLocation: LatLng?,
    driveDirPoints: List<LatLng>
) {
    Log.d(TAG, "DriveScreen: This is the loc $posLocation ")
    val cameraPositionState = rememberCameraPositionState {
        // if we have any position we will set it othewise it's just a blue screen of ocean
        posLocation?.let {
            position =
                CameraPosition.fromLatLngZoom(
                    LatLng(posLocation.latitude, posLocation.longitude),
                    15f
                )
        }
    }
    var isMapLoaded by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        GoogleMapView(
            modifier = modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                isMapLoaded = true
            },
            driveDirPoints = driveDirPoints
        )
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = modifier
                    .matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = modifier
                        .wrapContentSize()
                )
            }
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {},
    driveDirPoints: List<LatLng> = emptyList<LatLng>()
) {

    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        onPOIClick = {
            Log.d("GraphQL", "POI clicked: ${it.name}")
        }
    ) {
        Polyline(
            driveDirPoints,
            color = Color.Magenta
        )

        if (driveDirPoints.isNotEmpty()) {
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


@Preview
@Composable
fun GoogleMapViewPreview() {
    GoogleMapView(
        cameraPositionState = rememberCameraPositionState(),
        driveDirPoints = listOf(
            LatLng(37.423060, -122.084270),
            LatLng(37.421160, -122.084270)
        )
    )
}

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
    GoogleMapView()
}

// Tutorial
// https://www.youtube.com/watch?v=KDKF0b4cjF8
// https://lwgmnz.me/google-maps-and-directions-api-using-kotlin/