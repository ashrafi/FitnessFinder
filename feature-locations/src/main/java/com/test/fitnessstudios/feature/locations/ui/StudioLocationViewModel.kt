/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.fitnessstudios.feature.locations.ui


import android.Manifest
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.test.fitnessstudios.core.database.FitnessStudio
import com.test.fitnessstudios.core.domain.YelpCallUseCase
import com.test.fitnessstudios.core.model.model.FitLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// get last location
// https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapActivity.kt#L45

// in viewmodel Map state
// https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapViewModel.kt#L27


@HiltViewModel
class StudioLocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val yelpCall: YelpCallUseCase,
) : ViewModel() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    //var state by mutableStateOf(MapState())

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun lastLocation(): Flow<Location> = flow {
        fusedLocationClient.lastLocation.await()?.let { location ->
            setLocation(FitLocation(location.latitude, location.longitude))
            emit(location)
        }
    }


    val mapUI = MutableStateFlow(
        MapUiSettings(
            myLocationButtonEnabled = false,
            mapToolbarEnabled = true
        )
    )

    val maProp = MutableStateFlow(
        MapProperties(
            isMyLocationEnabled = false, // viewModel.test.value,
            maxZoomPreference = 15f,
            minZoomPreference = 10f
        )
    )

    var markers by mutableStateOf(
        arrayOf(
            LatLng(32.524155, -111.905792),
            LatLng(37.7749, -122.4194)
        )
    )
    //LatLng(37.7749, -122.4194))

    //val markers by mutableStateOf(testMarkers)

    val Scottsdale = FitLocation(33.524155, -111.905792)
    var testMyLocal = LatLng(33.524155, -111.905792)
    val curLocation = MutableStateFlow(Scottsdale)

    var testLocation by mutableStateOf(testMyLocal)


    val cameraPosition = MutableStateFlow<FitLocation?>(null)
    private val zoomLevel = MutableStateFlow<Float>(15.0f)

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(StudioLocationUiState.Success(emptyList()))

    val uiStateFit: StateFlow<StudioLocationUiState> = yelpCall
        .fitnessStudios.map { StudioLocationUiState.SuccessFitness(data = it) }
        .catch { Error(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StudioLocationUiState.Loading
        )

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<StudioLocationUiState> = _uiState
    //val feedUiState: StateFlow<NewsFeedUiState> = getSaveableNewsResources()

    init {
        // lastLocation()
        val singapore = LatLng(37.7749, -122.4194)
        callYelpAPI("fitness", singapore)
    }

    fun updateLocTest() {
        testLocation = LatLng(
            testLocation.latitude + Math.random() / 100,
            testLocation.longitude + Math.random() / 100
        )
        //markers = LatLng(testLocation.latitude + Math.random(), testLocation.longitude + Math.random())
        //markers = markers.plus(testLocation)
    }

    fun setLocation(loc: FitLocation) {
        curLocation.value = loc
        setCameraPosition(loc)
        testLocation = LatLng(loc.latitude, loc.longitude)
        // markers = markers.plus(testLocation)
        //markers = LatLng(loc.latitude + Math.random(), loc.longitude + Math.random())
        Log.d("Fitness", "size of array for points ${markers.size}")
        //getNearestStops(loc)
        callYelpAPI("fitness", testLocation)
    }

    fun setZoomLevel(zl: Float) {
        zoomLevel.value = zl
    }

    fun getZoomLevel(): Float {
        return zoomLevel.value ?: 15.0f
    }

    fun setCameraPosition(loc: FitLocation) {
        if (loc != cameraPosition.value) {
            cameraPosition.value = loc
            //getNearestStops(loc)
        }
    }

    fun add(gym: FitnessStudio) {
        viewModelScope.launch {
            yelpCall.add(gym)
        }
    }

    fun del() {
        viewModelScope.launch {
            yelpCall.del()
        }
    }

    fun callYelpAPI(cat: String, place: LatLng) {
        viewModelScope.launch {
            val businessList = yelpCall.invoke(
                category = cat,
                local = place
            )
            if (businessList == null) {
                // There were some error
                Log.d(TAG, "did not work")
                // TODO: do something with response.errors
                StudioLocationUiState.Error(Throwable("bad"))
            } else {
                _uiState.value = StudioLocationUiState.Success(businessList)
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun updatePermissions(locationPermissionsState: MultiplePermissionsState) {
        if (locationPermissionsState.allPermissionsGranted) {
            maProp.value = maProp.value.copy(
                isMyLocationEnabled = true, // viewModel.test.value,
                maxZoomPreference = 20f,
                minZoomPreference = 5f
            )

            mapUI.value = MapUiSettings(
                myLocationButtonEnabled = true,
                mapToolbarEnabled = true
            )
        }
    }

    val TAG = "GraphQL"
}
