package com.test.fitnessstudios.core.testing.data.repository

import android.location.Location
import com.test.fitnessstudios.core.data.repository.LocationClientRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FakeLocationClientRepo @Inject constructor() : LocationClientRepo {
    private val locations = mutableListOf<Location>()

    init {
        locations.add(Location("fakeLocation").apply {
            latitude = 37.7749
            longitude = -122.4194
            accuracy = 10.0f
        })
    }

    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return flow {
            while (true) {
                if (locations.isNotEmpty()) {
                    emit(locations.removeFirst())
                } else {
                    delay(interval)
                }
            }
        }
    }

    override fun getLastLocFlow(): MutableStateFlow<Location?> {
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
