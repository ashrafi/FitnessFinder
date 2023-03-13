package com.test.fitnessstudios.core.model.model

import kotlin.math.sqrt

data class FitLocation(var latitude: Double, var longitude: Double) {
    fun distance(that: FitLocation): Double {
        val distanceLat = this.latitude - that.latitude
        val distanceLong = this.longitude - that.longitude

        return sqrt(distanceLat * distanceLat + distanceLong * distanceLong)
    }
}