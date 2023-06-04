package com.test.fitnessstudios.core.model

data class LatLngModel(val latitude: Double, val longitude: Double) {
    fun toSerializedString(): String = "$latitude,$longitude"

    companion object {
        fun fromSerializedString(serializedString: String): LatLngModel {
            val parts = serializedString.split(",")
            val latitude = parts[0].toDouble()
            val longitude = parts[1].toDouble()
            return LatLngModel(latitude, longitude)
        }
    }
}
