package com.test.fitnessstudios.feature.details.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.test.fitnessstudios.core.domain.DriveUseCase
import com.test.fitnessstudios.core.domain.FitnessUseCase
import com.test.fitnessstudios.feature.details.ui.LocationDetailsUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fitness: FitnessUseCase,
    private val drivePts: DriveUseCase
) : ViewModel() {


    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(LocationDetailsUiState.SuccessFitness(emptyList()))


    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LocationDetailsUiState> = _uiState
    //var state by mutableStateOf(MapState())

    val locationDetailsUiState: StateFlow<LocationDetailsUiState> = fitness
        .fitnessStudios.map { SuccessFitness(data = it) }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun getImgUrl(): String? {
        return _uiState.value.data.first().photo
    }

    /*@RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun lastLocation(): Flow<Location> = flow {
        fusedLocationClient.lastLocation.await()?.let { location ->
            setLocation(FitLocation(location.latitude, location.longitude))
            emit(location)
        }
    }*/

    var drivingPoints = MutableStateFlow(emptyList<LatLng>())

    init {
        val org = "${(37.7749 + Math.random() / 100)},${-122.4194 + Math.random() / 100}"
        val des = "${(37.7749 + Math.random() / 100)},${-122.4194 + Math.random() / 100}"
        viewModelScope.launch(Dispatchers.IO) {
            drivingPoints.value = drivePts.getDrivePts(org, des)
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
    //LatLng(37.7749, -122.4194))

    //val markers by mutableStateOf(testMarkers)

    var testMyLocal = LatLng(33.524155, -111.905792)
    val curLocation = MutableStateFlow(testMyLocal)


    var testLocation by mutableStateOf(testMyLocal)

    val cameraPosition = MutableStateFlow(testMyLocal)


    // Backing property to avoid state updates from other classes
    //private val _uiState = MutableStateFlow(StudioLocationUiState.Success(emptyList()))

    /*val uiStateFit: StateFlow<StudioLocationUiState> = yelpCall
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
        //TODO: replace with last known location.
        val test_location = LatLng(37.7749, -122.4194)
        // lastLocation()
        callYelpAPI("fitness", test_location)
    }

    fun setLocation(loc: FitLocation) {
        curLocation.value = loc
        setCameraPosition(loc)
        testLocation = LatLng(loc.latitude, loc.longitude)
        // markers = markers.plus(testLocation)
        //markers = LatLng(loc.latitude + Math.random(), loc.longitude + Math.random())
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
    }*/

    val TAG = "GraphQL"


}