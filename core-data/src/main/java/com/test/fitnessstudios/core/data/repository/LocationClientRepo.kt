package com.test.fitnessstudios.data.data.repository

import android.location.Location
import kotlinx.coroutines.flow.MutableStateFlow

interface LocationClientRepo {
    fun getCurrentLocation(): MutableStateFlow<Location?>
}

// suspend fun getCurrentLocation(): Flow<LocatioinResult<Location?>>
