package com.test.fitnessstudios.core.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.test.fitnessstudios.core.data.repository.LocationClientRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class LocationClientImpl @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClientRepo {

    private var startLocation = Location("MyStartLoc").apply {
        latitude = 37.7749 // Set the latitude of the location
        longitude = -122.4194 // Set the longitude of the location
        accuracy = 10.0f // Set the accuracy of the location in meters
    }


    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 100000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(50000)
        .setMaxUpdateDelayMillis(100000)
        .build()

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): MutableStateFlow<Location?> {
        Log.d("GraphQL", "--- Getting the current Location Called !!!! ---")
        val currentLocation = MutableStateFlow<Location?>(null)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                currentLocation.value = location
                Log.d("GraphQL", "Location Updated --- we are in play")
            }
        }

        // Request location updates
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        return currentLocation
    }

}