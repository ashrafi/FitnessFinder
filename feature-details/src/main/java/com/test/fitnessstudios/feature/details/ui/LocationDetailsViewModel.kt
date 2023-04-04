package com.test.fitnessstudios.feature.details.ui

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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val yelpList: YelpGetUseCase,
    private val drivePts: DriveUseCase,
    private val currLoc: GetCurrentLocationUseCase
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LocationDetailsUiState.Success(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LocationDetailsUiState> = _uiState
    //var state by mutableStateOf(MapState())

    var drivingPoints = MutableStateFlow(emptyList<LatLng>())

    var myGym = LatLng(37.3861, -122.0839)
    var initLocal = LatLng(33.524155, -111.905792)
    val curLocation = MutableStateFlow(initLocal)

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
        viewModelScope.launch(Dispatchers.IO) {
            curLocation.collectLatest {
                curLocation.value = LatLng(it.latitude, it.longitude)
                drivingPoints.value =
                    drivePts.getDrivePts(orig = curLocation.value, des = des)
            }
        }
    }
}

const val TAG = "GraphQL"
