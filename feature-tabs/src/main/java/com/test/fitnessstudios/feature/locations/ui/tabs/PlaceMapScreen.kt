package com.test.fitnessstudios.feature.locations.ui.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.YelpCategory
import com.test.fitnessstudios.feature.locations.R
import com.test.fitnessstudios.feature.locations.ui.StudioLocationUiState
import com.test.fitnessstudios.feature.locations.ui.StudioLocationViewModel
import com.test.fitnessstudios.feature.locations.ui.TAG
import com.test.fitnessstudios.feature.locations.ui.map.LocationPermissions


/**
 * Google Maps showing the Yelp results as markers
 */
@Composable
fun PlaceMapScreen(
    modifier: Modifier = Modifier,
    viewModel: StudioLocationViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val currUiState by produceState<StudioLocationUiState>(
        initialValue = StudioLocationUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    val loc = viewModel.locationStateFlow.collectAsState()
    val cat = viewModel.currentCategoryFlow.collectAsState(initial = YelpCategory.fitness.name)


    Log.d(TAG, "PlaceMapScreen: 1 This is the Lat / Lan $loc")

    // Set the initial position of the camera
    val cameraPositionState = rememberCameraPositionState {
        loc?.value?.let {
            position = CameraPosition(LatLng(it.latitude, it.longitude), 15F, 0F, 0F)
        } ?: run { // If loc is null.
            position = CameraPosition(LatLng(33.524155, -111.905792), 15F, 0F, 0F)
        }
        Log.d(TAG, "PlaceMapScreen: 2 This is the Lat / Lan $loc")
    }

    /*LaunchedEffect(loc.value) {
        loc?.value?.let {
            cameraPositionState.position =
                CameraPosition(LatLng(it.latitude, it.longitude), 15F, 0F, 0F)
        }
        Log.d(TAG, "PlaceMapScreen: 2 This is the Lat / Lan $loc")
    }*/

    // LaunchedEffect: run suspend functions in the scope of a composable
    LaunchedEffect(cameraPositionState.isMoving) {
        val position = cameraPositionState.position
        val isMoving = cameraPositionState.isMoving

        if (!isMoving) {
            viewModel.setZoomLevel(cameraPositionState.position.zoom)
            viewModel.callYelpAPI(cat.value, position.target)
            viewModel.currentCameraPosition = position.target
        }
    }
    // 37.7749° N, 122.4194° W
    // Set properties using MapProperties which you can use to recompose the map
    var mapProperties by remember {
        mutableStateOf(viewModel.maProp)
    }

    var mapUiSettings by remember {
        mutableStateOf(viewModel.mapUI)
    }


    val saveCurrentCategory: (String) -> Unit = { category ->
        viewModel.saveStoredCurrentCategory(category)
    }

    val callYelpCategory: (String) -> Unit = { yelpCall ->
        viewModel.callYelpAPI(yelpCall)
    }

    when (currUiState) {
        is StudioLocationUiState.Loading -> {
            // Show loading spinner
            Text(currUiState.toString())
            Text("Showing Spinner")
        }

        is StudioLocationUiState.Success -> {
            PlaceMapScreen(
                modifier = modifier,
                items = (currUiState as StudioLocationUiState.Success).launchList,
                mapProperties.value,
                mapUiSettings.value,
                saveCurrentCategory,
                callYelpCategory,
                cat,
                cameraPositionState
            )
        }

        is StudioLocationUiState.Error -> {
            // Show error message
            Text(currUiState.toString())
        }

    }
}

@Composable
internal fun PlaceMapScreen(
    modifier: Modifier = Modifier,
    items: List<BusinessInfo?>?,
    mpp: MapProperties,
    mps: MapUiSettings,
    saveCat: (String) -> Unit,
    callYelp: (String) -> Unit,
    cat: State<String>,
    cp: CameraPositionState
) {
    // MutableStateFlow
    Column {
        LocationPermissions()
        CollapsibleView(
            saveCat = saveCat,
            callYelp = callYelp,
            cat = cat
        )
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                properties = mpp,
                uiSettings = mps,
                cameraPositionState = cp,
            ) {
                var markCat = BitmapDescriptorFactory.fromResource(R.drawable.gym)
                when (cat.value) {
                    YelpCategory.bars.name -> {
                        markCat = BitmapDescriptorFactory.fromResource(R.drawable.bar)
                    }

                    YelpCategory.food.name -> {
                        markCat = BitmapDescriptorFactory.fromResource(R.drawable.food)
                    }

                    YelpCategory.fitness.name -> {
                        markCat = BitmapDescriptorFactory.fromResource(R.drawable.gym)
                    }
                }
                // viewModel.state.parkingSpots.forEach
                val context = LocalContext.current
                items?.forEach {
                    it?.let { business ->
                        it.let { business ->
                            business.coordinates?.let { place ->
                                if (place.latitude != null && place.longitude != null) {
                                    Marker(
                                        state = MarkerState(
                                            position = LatLng(
                                                place.latitude!!,
                                                place.longitude!!
                                            )
                                        ),
                                        title = "${it.name}",
                                        draggable = false,
                                        icon = markCat,
                                        alpha = (0.9f),
                                        flat = (true),
                                        zIndex = (1.0f),
                                        onClick = {
                                            business.rating?.let { rating ->
                                                if (rating > 0.0) {
                                                    Toast.makeText(
                                                        context,
                                                        "Rating: ${business.rating}\n",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            false // return true to indicate that the event has been handled
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CollapsibleView(
    modifier: Modifier = Modifier,
    saveCat: (String) -> Unit,
    callYelp: (String) -> Unit,
    cat: State<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val carrotIcon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown
    val context = LocalContext.current

    var catIcon = painterResource(R.drawable.gym)
    when (cat.value) {
        YelpCategory.bars.name -> {
            catIcon = painterResource(R.drawable.bar)
        }

        YelpCategory.food.name -> {
            catIcon = painterResource(R.drawable.food)
        }

        YelpCategory.fitness.name -> {
            catIcon = painterResource(R.drawable.gym)
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Icon(
                imageVector = carrotIcon,
                contentDescription = null,
                //tint = MaterialTheme.colors.onSurface
            )
            Text(
                text = "Category ",
                modifier = Modifier.padding(start = 16.dp),
                //style = MaterialTheme.typography.subtitle1
            )
            Icon(
                painter = catIcon, "content description",
                modifier = Modifier.size(24.dp)
            )

        }

        if (expanded) {
            Column(modifier = modifier) {
                Row {
                    Button(onClick = {
                        saveCat(YelpCategory.fitness.name)
                        callYelp(YelpCategory.fitness.name)
                    }
                    ) {
                        Text("Fitness")
                    }
                    Button(onClick = {
                        saveCat(YelpCategory.food.name)
                        callYelp(YelpCategory.food.name)
                    }) {
                        Text("Food")
                    }
                    Button(onClick = {
                        saveCat(YelpCategory.bars.name)
                        callYelp(YelpCategory.bars.name)
                    }) {
                        Text("Bars")
                    }
                }

            }

            /*if (items_test is StudioLocationUiState.SuccessFitness) {
                myFavs = (items_test as StudioLocationUiState.SuccessFitness).data

                myFavs.forEach {
                    Text("Saved item: $it")
                }
            }*/
        }
    }
}

@Preview
@Composable
fun PlaceMapScreenPreview() {

    val mpp = MapProperties(
        isMyLocationEnabled = false, // viewModel.test.value,
        maxZoomPreference = 15f,
        minZoomPreference = 10f
    )

    val mps = MapUiSettings(
        myLocationButtonEnabled = false,
        mapToolbarEnabled = true
    )

    // Set the initial position of the camera
    val cameraPos = rememberCameraPositionState {
        position = CameraPosition(LatLng(33.524155, -111.905792), 15F, 0F, 0F)
    }

    val cat = remember { mutableStateOf(YelpCategory.bars.name) }
    PlaceMapScreen(
        items = null,
        mpp = mpp,
        mps = mps,
        saveCat = {},
        callYelp = {},
        cat = cat,
        cp = cameraPos
    )
}

@Preview
@Composable
fun CollapsibleViewPreview() {
    val cat = remember { mutableStateOf(YelpCategory.bars.name) }
    CollapsibleView(
        modifier = Modifier.fillMaxWidth(),
        saveCat = {},
        callYelp = {},
        cat = cat
    )
}


/*Column {
Button(onClick = {
    mapProperties = mapProperties.copy(
        isBuildingEnabled = !mapProperties.isBuildingEnabled
    )
}) {
    Text(text = "Toggle isBuildingEnabled")
}
Button(onClick = {
    mapUiSettings = mapUiSettings.copy(
        mapToolbarEnabled = !mapUiSettings.mapToolbarEnabled
    )
}) {
    Text(text = "Toggle mapToolbarEnabled")
}
}*/
