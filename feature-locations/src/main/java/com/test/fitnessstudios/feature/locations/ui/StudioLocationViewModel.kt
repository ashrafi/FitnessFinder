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


import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.test.fitnessstudios.core.domain.GetCurrentLocationUseCase
import com.test.fitnessstudios.core.domain.YelpCallUseCase
import com.test.fitnessstudios.core.model.model.FitLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// get last location
// https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapActivity.kt#L45

// in viewmodel Map state
// https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapViewModel.kt#L27

// How to setup ViewModel
// https://github.com/android/architecture-templates/blob/multimodule/feature-mymodel/src/main/java/android/template/feature/mymodel/ui/MyModelViewModel.kt

@HiltViewModel
class StudioLocationViewModel @Inject constructor(
    private val yelpCall: YelpCallUseCase,
    private val currLoc: GetCurrentLocationUseCase
) : ViewModel() {

    val mapUI = mutableStateOf(
        MapUiSettings(
            myLocationButtonEnabled = false,
            mapToolbarEnabled = true
        )
    )

    val maProp = mutableStateOf(
        MapProperties(
            isMyLocationEnabled = false, // viewModel.test.value,
            maxZoomPreference = 15f,
            minZoomPreference = 10f
        )
    )

    init {

    }

    val curLocation = MutableStateFlow(currLoc())

    val cameraPosition = mutableStateOf<FitLocation?>(null)
    private val zoomLevel = mutableStateOf<Float>(15.0f)

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(StudioLocationUiState.Success(emptyList()))

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

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<StudioLocationUiState> = _uiState
    // val feedUiState: StateFlow<NewsFeedUiState> = getSaveableNewsResources()


    /*fun setLocation(loc: FitLocation) {
        curLocation.value = loc
        setCameraPosition(loc)
        val testLocation = LatLng(loc.latitude, loc.longitude)
        callYelpAPI("fitness", testLocation)
    }*/

    /*
    Add when Fav is pressed
     */

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

    fun setCameraLocation(cameraLocation: FitLocation) {

    }
}

const val TAG = "GraphQL"
