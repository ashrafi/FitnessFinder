package com.test.fitnessstudios.feature.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.Category
import com.test.fitnessstudios.core.model.model.Coordinates
import com.test.fitnessstudios.feature.details.ui.LocationDetailsUiState.Loading
import com.test.fitnessstudios.feature.details.ui.drive.DriveScreen
import com.test.fitnessstudios.feature.details.ui.info.LocImg

@Composable
fun LocationDetailsScreen(
    modifier: Modifier = Modifier,
    busInfoID: String,
    viewModel: LocationDetailsViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val items by produceState<LocationDetailsUiState>(
        initialValue = Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    val drivePts = viewModel.drivingPoints.collectAsState().value
    var currLoc = viewModel.locationStateFlow.collectAsState().value?.let { loc ->
        LatLng(loc.latitude, loc.longitude)
    }


    if (items is LocationDetailsUiState.Success) {
        val found = (items as LocationDetailsUiState.Success).launchList?.find { busInfo ->
            busInfo?.id == busInfoID
        }
        found?.let { busInfo ->
            val coordinates: Coordinates? = busInfo.coordinates
            val corrLat: Double? = coordinates?.latitude
            val coorLng: Double? = coordinates?.longitude
            if (corrLat != null && coorLng != null) {
                viewModel.updateDrivePts(LatLng(corrLat, coorLng))
                if (currLoc == null)
                    currLoc = LatLng(corrLat,coorLng)
            }
            LocationDetailsScreen(
                modifier,
                busInfo,
                drivePts,
                currLoc
            )
        } ?: run {
            Text("Business not found ...")
        }
    } else {
        Text("Loading ...")
    }
}

@Composable
internal fun LocationDetailsScreen(
    modifier: Modifier = Modifier,
    bf: BusinessInfo,
    driveDirPoints: List<LatLng>,
    currLoc: LatLng?
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(color = Yellow),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {

            LocImg(
                photoURL = bf.photos?.first(),
                name = bf.name ?: "",
                webURL = bf.url ?: "No web address"
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(color = Yellow),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            DriveScreen(
                modifier,
                posLocation = currLoc,
                driveDirPoints = driveDirPoints,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LocationDetailsScreenPreview() {
    val businessInfo = BusinessInfo(
        id = "ABC123",
        name = "The Best Coffee Shop",
        url = "https://www.example.com/coffeeshop",
        rating = 4.5,
        photos = listOf(
            "https://www.example.com/coffeeshop/photo1.jpg",
            "https://www.example.com/coffeeshop/photo2.jpg",
            null
        ),
        price = "$$",
        coordinates = Coordinates(
            latitude = 37.7749,
            longitude = -122.4194
        ),
        categories = listOf(
            Category(title = "Cafe")
        )
    )

    val driveDirPoints = listOf(
        LatLng(37.7749, -122.4194),
        LatLng(37.7749, -122.4196),
        LatLng(37.7747, -122.4194),
        LatLng(37.7747, -122.4196),
    )
    val currLoc = LatLng(37.7749, -122.4194)

    LocationDetailsScreen(
        modifier = Modifier,
        bf = businessInfo,
        driveDirPoints = driveDirPoints,
        currLoc = currLoc
    )
}