package com.test.fitnessstudios.core.domain

import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.core.data.repository.MapsRepository
import javax.inject.Inject


class DriveUseCase @Inject constructor(
    private val mapsRepository: MapsRepository,
) {

    suspend fun getDrivePts(): List<LatLng> {
        return mapsRepository.getDrivingPts()
    }

}