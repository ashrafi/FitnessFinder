package com.test.fitnessstudios.core.data

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.MapsRepository
import com.test.fitnessstudios.core.network.model.MapsAPI
import javax.inject.Inject

class MapRepImp @Inject constructor(
    private val mapCall: MapsAPI
) : MapsRepository {
    override suspend fun getDrivingPts(): List<LatLng> {
        // val jsonString = mapCall.getMapDirections()

        val holder = arrayListOf(
            LatLng(37.7749, -122.4194),
            LatLng(37.7749 + Math.random() / 100, -122.4194 + Math.random() / 100)
        )

        return holder
    }

}