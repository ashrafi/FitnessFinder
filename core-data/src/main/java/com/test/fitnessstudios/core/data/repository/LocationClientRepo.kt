package com.test.fitnessstudios.core.data.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocationClientRepo {
    fun getLocationUpdates(interval: Long): Flow<Location>

    fun getLastLocFlow(): MutableStateFlow<Location?>

    class LocationException(message: String) : Exception()
}

// suspend fun getCurrentLocation(): Flow<LocatioinResult<Location?>>
