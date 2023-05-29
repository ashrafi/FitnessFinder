package com.test.fitnessstudios.feature.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.model.BusinessInfo
import com.test.fitnessstudios.core.model.Category
import com.test.fitnessstudios.core.model.Coordinates
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

    if (items is LocationDetailsUiState.Success) {
        val found = (items as LocationDetailsUiState.Success).launchList?.find { busInfo ->
            busInfo?.id == busInfoID
        }
        found?.let { busInfo ->
            val coordinates: Coordinates? = busInfo.coordinates
            val coorLat: Double? = coordinates?.latitude
            val coorLng: Double? = coordinates?.longitude
            if (coorLat != null && coorLng != null) {
                viewModel.buslocation.value = LatLng(coorLat, coorLng)
            }
            LocationDetailsScreen(
                modifier,
                busInfo
            )
        } ?: run {
            Text("Business not found ...")
        }
    } else {
        Box(
            modifier = modifier
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
internal fun LocationDetailsScreen(
    modifier: Modifier = Modifier,
    bf: BusinessInfo,
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
                .background(color = Gray),
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
                .background(color = Gray),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            DriveScreen()
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
        businessInfo.id
    )
}