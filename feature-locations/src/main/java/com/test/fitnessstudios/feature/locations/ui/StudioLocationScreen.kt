package com.test.fitnessstudios.feature.locations.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.test.fitnessstudios.core.network.SearchYelpQuery
import com.test.fitnessstudios.core.network.service.apolloClient
import kotlinx.coroutines.flow.map

@Composable
fun StudioLocationScreen(
    modifier: Modifier = Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    Box(
        modifier.fillMaxSize()
    ) {
        Button(onClick = { viewModel.callQL() }) {
            Text(text = "Send")
        }
    }
}

@Composable
internal fun GymList(
    modifier: Modifier = Modifier.fillMaxSize(),
    alignment: Alignment.Vertical
) {
    val context = LocalContext.current
    // tell Compose to remember the flow across recompositions
    
}


