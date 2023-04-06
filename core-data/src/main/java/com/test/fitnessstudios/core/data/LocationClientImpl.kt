package com.test.fitnessstudios.core.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.test.fitnessstudios.core.data.repository.LocationClientRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class LocationClientImpl @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClientRepo {

    var startLocation = Location("MyStartLoc")

    init {
        startLocation.latitude = 37.7749 // Set the latitude of the location
        startLocation.longitude = -122.4194 // Set the longitude of the location
        startLocation.accuracy = 10.0f // Set the accuracy of the location in meter
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {


        return callbackFlow {
            /*if(!context.hasLocationPermission()) {
                throw LocationClientRepo.LocationException("Missing location permission")
            }*/

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClientRepo.LocationException("GPS is disabled")
            }

            val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 100000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(50000)
                .setMaxUpdateDelayMillis(100000)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    override fun getLastLocFlow(): Flow<Location> = flow {
        try {
            // Get the last known location from the fusedLocationClient
            val location: Location? = fusedLocationClient.lastLocation.await()

            // Emit the location to the flow
            location?.let {
                emit(location)
            }
        } catch (e: Exception) {
            // Handle any errors that may occur while getting the last known location
            // For example, if location services are disabled or permissions are denied
            // You can customize the error handling based on your app's requirements
        }
    }

    fun getLastLocOrDefaultOld(): Location {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                startLocation = location
            }
            Log.d("GraphQL", "getLastLocOrDefault: This is my current fused location: $location")
        }
        Log.d("GraphQL", "getLastLocOrDefault:  current location is null: $startLocation")
        return startLocation
    }


    @ExperimentalCoroutinesApi
    fun FusedLocationProviderClient.locationFlow(locationRequest: LocationRequest): Flow<LocationResult> =
        callbackFlow {
            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    trySend(result).isSuccess // Offer the location result to the channel
                }
            }

            // Request location updates
            requestLocationUpdates(locationRequest, callback, null)

            // Remove location updates when the flow is cancelled or closed
            awaitClose {
                removeLocationUpdates(callback)
            }
        }
}