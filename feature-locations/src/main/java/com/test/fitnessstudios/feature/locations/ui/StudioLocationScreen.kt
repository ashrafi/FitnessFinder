package com.test.fitnessstudios.feature.locations.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.test.fitnessstudios.core.database.FitnessStudio
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun StudioLocationScreen(
    modifier: Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    HorizontalPagerScreen(modifier)
}

//@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StudioLocationScreenHold(
    modifier: Modifier = Modifier.padding(all = 0.dp),
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    var myFavs: List<FitnessStudio> = emptyList()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val items_test by produceState<UiState>(
        initialValue = UiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiStateFit.collect { value = it }
        }
    }


    /**
     * collectAsStateWithLifecycle is a composable function that collects values from a flow and
     * represents the latest value as Compose State in a lifecycle-aware manner.
     */
    // collectAsState will turn our Flow into state that can be consumed by Composables
    val state = viewModel.uiState.collectAsState(initial = UiState.Loading)

    Column(modifier = modifier) {
        Row {
            Button(onClick = { viewModel.callYelpAPI("food") }) {
                Text("Food")
            }
            Button(onClick = { viewModel.callYelpAPI("fitness") }) {
                Text("Fitness")
            }
            Button(onClick = { viewModel.callYelpAPI("bars") }) {
                Text("Nothing")
            }
        }

        if (items_test is UiState.SuccessFitness) {
            myFavs = (items_test as UiState.SuccessFitness).data

            myFavs.forEach {
                Text("Saved item: $it")
            }
        }

        when (val value = state.value) {
            is UiState.Success -> LazyColumn {
                items(value.launchList ?: emptyList()) {
                    Text(it?.name.toString())
                    Text(it?.id.toString())
                    it?.id.let { busID ->
                        FavoriteButton(
                            fav = myFavs.contains(myFavs.find { favList ->
                                favList.uid == busID
                            }),
                            //fav = myFavs.forEach.contains(it?.id)
                            add = {
                                Log.d("GraphQL", "CAlled Add")
                                it?.let {
                                    viewModel.add(
                                        FitnessStudio(
                                            it.id, it?.name ?: "gym",
                                            Clock.System.now()
                                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                        )
                                    )
                                }
                            },
                            del = {
                                viewModel.del()
                            }
                        )
                    }
                    AsyncImage(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        model = it?.photos?.first(), contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    //Log.d("GraphQL", "this is it ${it.name}")
                }
            }
            else -> {
                Text("No Items found")
            }
        }
    }
}


@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63),
    add: () -> Unit,
    del: () -> Unit,
    fav: Boolean,
) {
    Row() {
        Button(onClick = { add() }) {
            Text("Add ${fav}")
        }
        //var isFavorite by remember { mutableStateOf(false) }

        IconToggleButton(
            checked = fav,
            onCheckedChange = {
                if (!fav)
                    add()
                else
                    del()
            }
        ) {
            Icon(
                tint = color,
                modifier = modifier.graphicsLayer {
                    scaleX = 1.3f
                    scaleY = 1.3f
                },
                imageVector = if (fav) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }
    }

}