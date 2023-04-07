package com.test.fitnessstudios.feature.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.test.fitnessstudios.feature.details.ui.LocationDetailsUiState.Loading
import com.test.fitnessstudios.feature.details.ui.drive.DriveScreen
import com.test.fitnessstudios.feature.details.ui.info.LocImg

@Composable
fun LocationDetailsScreen(
    modifier: Modifier = Modifier,
    id: String,
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

    val currLoc = viewModel.locationStateFlow.collectAsState().value?.let { loc ->
        LatLng(loc.latitude, loc.longitude)
    }

    val drivePts = viewModel.drivingPoints.collectAsState().value



    if (items is LocationDetailsUiState.Success) {
        val found = (items as LocationDetailsUiState.Success).launchList?.find { busInfo ->
            busInfo?.id == id
        }
        found?.let { busInfo ->
            busInfo.coordinates?.let {
                viewModel.updateDrivePts(LatLng(it.latitude!!, it.longitude!!))
                LocationDetailsScreen(
                    modifier = modifier,
                    bf = busInfo,
                    driveDirPoints = drivePts,
                    currLoc ?: LatLng(37.7749, -122.4194),
                )
            }
        }
    }
}

@Composable
internal fun LocationDetailsScreen(
    modifier: Modifier = Modifier,
    bf: BusinessInfo,
    driveDirPoints: List<LatLng>,
    currLoc: LatLng
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(Yellow),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            bf.photos?.let { photos ->
                LocImg(photoURL = photos.first())
            }
        }
        Text("This is the id ${bf.id}")
        Row(
            Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(Yellow),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            DriveScreen(
                driveDirPoints = driveDirPoints,
                posLocation = currLoc,
            )
        }
    }
}

@Preview
@Composable
fun LocationDetailsScreenPreview() {
    //val img = Image("one")
    //LocationDetailsScreen()
}