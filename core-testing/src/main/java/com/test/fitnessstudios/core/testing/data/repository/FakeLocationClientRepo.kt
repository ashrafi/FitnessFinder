package com.test.fitnessstudios.core.testing.data.repository

import android.location.Location
import com.test.fitnessstudios.core.data.repository.LocationClientRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeLocationClientRepo @Inject constructor() : LocationClientRepo {
    private val locations = mutableListOf<Location>()

    init {
        locations.add(
            Location("fakeLocation").apply {
                latitude = 37.7749
                longitude = -122.4194
                accuracy = 10.0f
            }
        )
    }

    override fun getCurrentLocation(): MutableStateFlow<Location?> {
        val lastLocationFlow: MutableStateFlow<Location?> = MutableStateFlow(
            Location("FakeLocation").apply {
                latitude = 37.7749
                longitude = -122.4194
                accuracy = 10.0f
            }
        )

        return lastLocationFlow
    }

    fun addLocation(location: Location) {
        locations.add(location)
    }
}
