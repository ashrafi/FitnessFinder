package com.test.fitnessstudios.core.network.service.maps

import com.test.fitnessstudios.core.network.BuildConfig.MAPS_API_KEY
import com.test.fitnessstudios.core.network.model.MapsAPI
import java.net.URL

class MapsClient : MapsAPI {
    override fun getMapDirections(): String {
        // https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=<YOUR_API_KEY>
        val base = "https://maps.googleapis.com/maps/api/directions/json"
        val org = "origin=10.3181466,123.9029382"
        val des = "destination=10.311795,123.915864"
        val call = "$base?$org&$des&key=$MAPS_API_KEY"
        return URL(call).readText()
    }
}