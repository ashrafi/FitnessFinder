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


import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.test.fitnessstudios.core.domain.DataStoreUseCase
import com.test.fitnessstudios.core.domain.FitnessUseCase
import com.test.fitnessstudios.core.domain.GetCurrentLocationUseCase
import com.test.fitnessstudios.core.domain.YelpCallUseCase
import com.test.fitnessstudios.core.model.model.BusinessInfo
import com.test.fitnessstudios.core.model.model.LatLngModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val currLoc: GetCurrentLocationUseCase,
    private val fitCase: FitnessUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : ViewModel() {

    private val _locationStateFlow = MutableStateFlow<Location?>(null)
    val locationStateFlow: StateFlow<Location?> get() = _locationStateFlow

    var currentCameraPosition: LatLng? = null

    // NOTE: MOVE ALL TO use case!!!
    fun saveCategory(cat: String) {
        viewModelScope.launch {
            dataStoreUseCase.saveCategory(cat)
        }
    }

    fun getCategory(): Flow<String> {
        return dataStoreUseCase.getCategory()
    }

    fun readTab(): Flow<Int> {
        return dataStoreUseCase.readTab()
    }

    fun saveTab(tabIndx: Int) {
        viewModelScope.launch {
            dataStoreUseCase.saveTab(tabIndx)
        }
    }

    suspend fun saveLatLngs(place: LatLng) {
        Log.d(TAG, "Save Lat / Lang $place")
        dataStoreUseCase.saveLatLng(place)
    }

    fun readLatLng(): Flow<Set<LatLngModel>> {
        return dataStoreUseCase.readLatLng()
    }

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

    val zoomLevel = mutableStateOf<Float>(15.0f)


    fun containsFav(id: String): Flow<Boolean?> {
        return fitCase.itemExistsById(id)
    }

    fun addFavBus(busInfo: BusinessInfo) {
        viewModelScope.launch {
            // Find the business and add to the list in UseCase
            fitCase.add(busInfo)
        }
    }

    fun delFavBus(busInfo: BusinessInfo) {
        // Find the business and add to the list
        viewModelScope.launch {
            fitCase.deleteItemById(busInfo.id)
        }
    }


    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(StudioLocationUiState.Success(emptyList()))

    fun initYelpAPI(cat: String, place: LatLngModel) {
        callYelpAPI(cat, LatLng(place.latitude, place.longitude))
    }

    fun callYelpAPI(cat: String) {
        val place: LatLng? = currentCameraPosition
        place?.let {
            callYelpAPI(cat, LatLng(it.latitude, it.longitude))
        }
    }

    fun callYelpAPI(cat: String, place: LatLng) {

        viewModelScope.launch(Dispatchers.IO) {

            Log.d(TAG, "YELP Called")
            val businessList = yelpCall.invoke(category = cat, local = place)
            Log.d(TAG, "YELP Returned")

            if (businessList == null) {
                // There were some error
                Log.d(TAG, "did not work")
                // TODO: do something with response.errors
                StudioLocationUiState.Error(Throwable("bad"))
            } else {
                _uiState.value = StudioLocationUiState.Success(businessList)
                Log.d(TAG, _uiState.value.launchList.toString())
            }
        }
    }

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<StudioLocationUiState> = _uiState
    // val feedUiState: StateFlow<NewsFeedUiState> = getSaveableNewsResources()

    fun setZoomLevel(zl: Float) {
        zoomLevel.value = zl
    }

    fun getZoomLevel(): Float {
        return zoomLevel.value ?: 15.0f
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


    // Function to convert Location to LatLng
    fun convertLocationToLatLng(location: Location): LatLng {
        val lat = location.latitude
        val lng = location.longitude
        return LatLng(lat, lng)
    }

    // Function to convert LatLng to Location
    fun convertLatLngToLocation(latLng: LatLng): Location {
        val location = Location("currLocation")
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude
        return location
    }


}

const val TAG = "GraphQL"
