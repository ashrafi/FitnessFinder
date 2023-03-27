package com.test.fitnessstudios.feature.details.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.feature.details.ui.LocationDetailsUiState
import com.test.fitnessstudios.feature.details.ui.LocationDetailsViewModel

@Composable
fun LocImg(
    modifier: Modifier = Modifier,
    viewModel: LocationDetailsViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val items by produceState<LocationDetailsUiState>(
        initialValue = LocationDetailsUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.locationDetailsUiState.collect { value = it }
        }
    }

    if (items is LocationDetailsUiState.SuccessFitness) {
        LocImg(
            modifier,
            (items as LocationDetailsUiState.SuccessFitness).data.first(),
        )

    }
}

@Composable
internal fun LocImg(
    modifier: Modifier = Modifier,
    fs: FitnessStudio,
) {
    Column() {
        AsyncImage(
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(9f / 16f),
            model = fs.photo,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
    //Log.d("GraphQL", "this is it ${it.name}")
}