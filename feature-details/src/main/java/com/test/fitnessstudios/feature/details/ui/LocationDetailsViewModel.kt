package com.test.fitnessstudios.feature.details.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.test.fitnessstudios.core.domain.DriveUseCase
import com.test.fitnessstudios.core.domain.GetCurrentLocationUseCase
import com.test.fitnessstudios.core.domain.YelpGetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val yelpList: YelpGetUseCase,
    private val drivePts: DriveUseCase,
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

    var currUserLoc = currLoc.getLocUpdates()

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LocationDetailsUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LocationDetailsUiState> = _uiState
    //var state by mutableStateOf(MapState())

    var drivingPoints = mutableStateOf(emptyList<LatLng>())
    var buslocation = mutableStateOf<LatLng?>(null)

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val businessList = yelpList.invoke()
            if (businessList == null) {
                // There were some error
                Log.d(TAG, "did not work")
                // TODO: do something with response.errors
                LocationDetailsUiState.Error(Throwable("bad"))
            } else {
                _uiState.value = LocationDetailsUiState.Success(businessList)
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

            mapUI.value = mapUI.value.copy(
                myLocationButtonEnabled = true,
                mapToolbarEnabled = true
            )
        }

        // Once we have permissions we have to ask for the user location again
        currUserLoc = currLoc.getLocUpdates()
    }

    fun updateDrivePts(userLoc:LatLng, busiLoc : LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            drivingPoints.value = drivePts.getDrivePts(orig = userLoc, des = busiLoc)
        }
    }

}


const val TAG = "GraphQL"
