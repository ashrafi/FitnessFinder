package com.test.fitnessstudios.feature.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
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
            viewModel.locationDetailsUiState.collect { value = it }
        }
    }

    if (items is LocationDetailsUiState.SuccessFitness) {
        LocationDetailsScreen()
    }


}

@Composable
internal fun LocationDetailsScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(Blue),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            LocImg()
        }
        Row(
            Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(Blue),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            DriveScreen()
        }
    }
}

@Preview
@Composable
fun LocationDetailsScreenPreview() {
    //val img = Image("one")
    //LocationDetailsScreen()
}