package com.test.fitnessstudios.core.data

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.DrivingPtsRepository
import com.test.fitnessstudios.core.network.model.MapsAPI
import javax.inject.Inject

class DrivingPtsRepImp @Inject constructor(
    private val mapCall: MapsAPI
) : DrivingPtsRepository {

    override suspend fun getDrivingPts(org: String, des: String): String {
        val directionsString = mapCall.getMapDirections(org, des)
        Log.d("GraphQL", "getDrivingPts: directionsString")
        return directionsString
    }

}