package com.test.fitnessstudios.core.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.LocationClientRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationClientRepo
) {
    fun getLocUpdates(): MutableStateFlow<Location?> = locationRepository.getCurrentLocation()

    // Function to convert LatLng to Location
    fun convertLatLngToLocation(latLng: LatLng): Location {
        val location = Location("LastUserLocation")
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude
        return location
    }

}
