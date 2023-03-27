package com.test.fitnessstudios.core.domain

import android.location.Location
import com.test.fitnessstudios.core.data.repository.LocationClientRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationClientRepo
) {
    operator fun invoke(): Flow<Location> = locationRepository.getLocationUpdates(10000L)
}
