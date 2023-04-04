package com.test.fitnessstudios.core.domain

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.DrivingPtsRepository
import com.test.fitnessstudios.core.domain.util.DirectionsParser
import javax.inject.Inject


class DriveUseCase @Inject constructor(
    private val mapsRepository: DrivingPtsRepository,
) {
    suspend fun getDrivePts(orig: LatLng, des: LatLng): List<LatLng> {

        val origString = "${orig.latitude},${orig.longitude}"
        val distString = "${des.latitude},${des.longitude}"
        val drivingJsonString = mapsRepository.getDrivingPts(origString, distString)
        var drivePts = emptyList<LatLng>()
        DirectionsParser.parse(drivingJsonString)?.let {
            drivePts = it
        }
        return drivePts
    }
}
//"origin=${(37.7749 + Math.random()/100 )},${-122.4194  + Math.random()/100 }"
//"destination=${(37.7749 + Math.random()/100 )},${-122.4194  + Math.random()/100 }"
