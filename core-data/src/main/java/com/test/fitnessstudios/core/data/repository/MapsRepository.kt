package com.test.fitnessstudios.core.data.repository

import com.google.android.gms.maps.model.LatLng


interface MapsRepository {
    suspend fun getDrivingPts(): List<LatLng>
}