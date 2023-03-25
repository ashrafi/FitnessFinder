package com.test.fitnessstudios.core.domain

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.MapsRepository
import com.test.fitnessstudios.core.domain.util.DirectionsParser
import javax.inject.Inject


class DriveUseCase @Inject constructor(
    private val mapsRepository: MapsRepository,
) {

    suspend fun getDrivePts(org: String, des: String): List<LatLng> {
        val drivingJsonString = mapsRepository.getDrivingPts(org, des)
        var drivePts = emptyList<LatLng>()
        DirectionsParser.parse(drivingJsonString)?.let {
            drivePts = it
        }
        return drivePts
    }
}