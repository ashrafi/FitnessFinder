package com.test.fitnessstudios.feature.details.ui.drive

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun DriveScreen(
    modifier: Modifier = Modifier,
    posLocation: LatLng,
    driveDirPoints: List<LatLng>
) {
    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(LatLng(posLocation.latitude, posLocation.longitude), 15f)
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
        driveDirPoints.forEach { place ->
            Marker(
                state = rememberMarkerState(position = place),
                title = "Marker1",
                snippet = "Marker in Singapore",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
        }
    }
}


/**
 * Compose can not preview Google Maps
 */
@Preview
@Composable
fun GoogleMapViewPreview() {
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