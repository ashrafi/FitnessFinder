package com.test.fitnessstudios.core.data.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClientRepo {
    fun getLocationUpdates(interval: Long): Flow<Location>

    fun getLastLocFlow(): Flow<Location>
    class LocationException(message: String) : Exception()
}

// suspend fun getCurrentLocation(): Flow<LocatioinResult<Location?>>
