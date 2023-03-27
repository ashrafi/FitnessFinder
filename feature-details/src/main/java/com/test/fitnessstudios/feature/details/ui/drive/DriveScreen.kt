package com.test.fitnessstudios.feature.details.ui.drive

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.test.fitnessstudios.feature.details.ui.LocationDetailsViewModel


@Composable
fun DriveScreenMap() {
    val context = LocalContext.current
    Box(Modifier.fillMaxSize()) {
        Button(onClick = {
            drawTrack("Louisville", "old louisville", context)
        }) {
            Text(text = "Google map Draw Track")
        }
    }
}


@Composable
fun DriveScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationDetailsViewModel = hiltViewModel()
) {

    var mapProperties by remember { mutableStateOf(viewModel.maProp) }
    var mapUiSettings by remember { mutableStateOf(viewModel.mapUI) }

    // MutableStateFlow
    val posLocationVM by viewModel.curLocation.collectAsState()
    val posLocationHold = LatLng(37.7749, -122.4194)

    val posLocation by viewModel.curLocation.collectAsState()

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
            driveDirPoints = viewModel.drivingPoints.collectAsState().value
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
        Row() {
            Button(onClick = {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(
                        posLocation.latitude,
                        posLocation.longitude
                    ), 15f
                )
            }) {
                Text("update")
            }

            Text("This is the location $posLocation")
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


private fun drawTrack(source: String, destination: String, context: Context) {
    try {
        // create a uri
        val uri: Uri = Uri.parse("https://www.google.co.in/maps/dir/$source/$destination")
        // initializing a intent with action view.
        val i = Intent(Intent.ACTION_VIEW, uri)
        // below line is to set maps package name
        i.setPackage("com.google.android.apps.maps")
        // below line is to set flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // start activity
        context.startActivity(i)
    } catch (e: ActivityNotFoundException) {
        // when the google maps is not installed on users device
        // we will redirect our user to google play to download google maps.
        val uri: Uri =
            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
        // initializing intent with action view.
        val i = Intent(Intent.ACTION_VIEW, uri)
        // set flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // to start activity
        context.startActivity(i)
    }
}


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