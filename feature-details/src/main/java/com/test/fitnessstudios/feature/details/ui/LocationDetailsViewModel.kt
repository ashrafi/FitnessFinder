package com.test.fitnessstudios.feature.details.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
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

    private val _locationStateFlow = MutableStateFlow<Location?>(null)
    val locationStateFlow: StateFlow<Location?> get() = _locationStateFlow


    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LocationDetailsUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LocationDetailsUiState> = _uiState
    //var state by mutableStateOf(MapState())

    var drivingPoints = MutableStateFlow(emptyList<LatLng>())

    init {
        viewModelScope.launch(Dispatchers.Default) {
            currLoc().collect { location ->
                _locationStateFlow.value = location
            }

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

    fun getPic(id: String): String? {
        var photo: String? = null
        // if we find the business try to get the picture.
        _uiState.value.launchList?.find { bus ->
            bus?.id == id
        }?.photos?.let { photoList ->
            photo = photoList.first()
        }
        return photo
    }

    fun updateDrivePts(des: LatLng) {
        val loc = locationStateFlow.value
        loc?.let {
            val start = LatLng(it.latitude, it.longitude)
            viewModelScope.launch(Dispatchers.IO) {
                drivingPoints.value =
                    drivePts.getDrivePts(orig = start, des = des)
            }
        }
    }


}


const val TAG = "GraphQL"
