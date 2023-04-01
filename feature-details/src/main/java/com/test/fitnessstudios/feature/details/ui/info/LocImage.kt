package com.test.fitnessstudios.feature.details.ui.info

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.test.fitnessstudios.feature.details.ui.TAG

@Composable
fun LocImg(
    modifier: Modifier = Modifier,
    id: String,
    viewModel: LocationDetailsViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // Launch the effect and record the outcome of type LocationDetailsUiState if lifecycle or viewModel change
    val items by produceState<LocationDetailsUiState>(
        initialValue = LocationDetailsUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.locationDetailsUiState.collect { value = it }
        }
    }


    val currUiState by viewModel.uiState.collectAsState()
    Log.d("GraphQL", "this is the State ${currUiState}")

    when (items) {
        is LocationDetailsUiState.Loading -> LoadingState()
        is LocationDetailsUiState.SuccessFitness -> SuccessFitnessState(
            items = items as LocationDetailsUiState.SuccessFitness,
            id = id
        )
        is LocationDetailsUiState.Error -> ErrorState()
        else -> NoState()
    }
}

@Composable
fun SuccessFitnessState(
    modifier: Modifier = Modifier,
    items: LocationDetailsUiState.SuccessFitness,
    id: String
) {
    Log.d(TAG, "SuccessFitnessState: Looking for $id")
    val fin = items.data.find {
        Log.d(TAG, "SuccessFitnessState: ${it.name} / id ${it.uid}")
        it.uid == id
    }

    fin?.let {
        LocImg(
            modifier,
            it
        )
    }
}

@Composable
fun ErrorState() {
    Text("Error")
}

@Composable
fun NoState() {
    Text("Unknown State")
}

@Composable
fun LoadingState() {
    Text("Loading")
}

@Composable
internal fun LocImg(
    modifier: Modifier = Modifier,
    fs: FitnessStudio,
) {
    Box {
        Text("This is the id ${fs.uid}")
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
